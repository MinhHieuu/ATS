# DOCUMENT API — ATS (Applicant Tracking System)

Tài liệu mô tả toàn bộ REST API hiện có trong project, dùng để mapping với front-end.

- **Base URL mặc định:** `http://localhost:8080` (`server.port` = `SERVER_PORT`, mặc định `8080`)
- **Content-Type:** `application/json` (trừ upload file dùng `multipart/form-data`)
- **Kiểu ngày giờ:** `Instant` — serialize thành **epoch seconds dạng số** (ví dụ `1750598400.123456789`) vì project **không** cấu hình `spring.jackson.serialization.write-dates-as-timestamps=false`. FE cần parse số này (`new Date(seconds * 1000)`), không phải chuỗi ISO.
- **Số tiền (`salaryMin`, `salaryMax`):** `BigDecimal` → serialize thành số JSON.

---

## 1. Quy ước chung

### 1.1. Envelope thành công — `ApiResponse<T>`

Mọi response **thành công** đều được bọc trong cấu trúc sau:

```json
{
  "message": "success",
  "data": { }
}
```

| Trường | Kiểu | Mô tả |
|---|---|---|
| `message` | string | Thông điệp: `"success"`, `"create success"`, `"login success"`, `"refresh token success"`, `"logout success"`, `"delete success"`, `"create file success"` |
| `data` | T \| null | Payload thật sự. `null` với các API không trả dữ liệu (logout, delete, change password) |

> **Lưu ý FE:** luôn đọc dữ liệu qua `response.data.data`, không phải `response.data`.

### 1.2. Envelope lỗi — `ApiError`

Response **lỗi** **KHÔNG** dùng envelope trên, mà có cấu trúc riêng:

```json
{
  "timestamp": 1750598400.123456789,
  "status": 400,
  "error": "Bad Request",
  "message": "Request validation failed",
  "details": {
    "email": "Email must be valid",
    "fullname": "Full name is required"
  }
}
```

| Trường | Kiểu | Mô tả |
|---|---|---|
| `timestamp` | number | Thời điểm lỗi (epoch seconds) |
| `status` | number | HTTP status code |
| `error` | string | Reason phrase của status (`"Bad Request"`, `"Not Found"`, `"Conflict"`…) |
| `message` | string | Mô tả lỗi |
| `details` | object | Map `tên field → thông báo lỗi`. Rỗng `{}` nếu không phải lỗi validation |

### 1.3. Bảng ánh xạ exception → HTTP status

| Nguyên nhân | Status | `message` mẫu |
|---|---:|---|
| `MethodArgumentNotValidException` (Bean Validation `@Valid`) | **400** | `Request validation failed` + `details` từng field |
| `HttpMessageNotReadableException` (JSON sai, enum không hợp lệ) | **400** | `Malformed request or unsupported enum value` |
| `IllegalArgumentException` | **400** | Message của exception |
| Chưa đăng nhập / token sai, hết hạn | **401** | **Body rỗng** (`HttpStatusEntryPoint`) |
| Sai role (ví dụ CANDIDATE gọi `/api/admin/**`) | **403** | Body mặc định của Spring Security |
| `ResourceNotFoundException` | **404** | `Job not found`, `Candidate not found`, `User not found with id: 5`… |
| `BusinessRuleException` | **409** | `Email already exists`, `Old password does not match`, `Invalid refresh token`… |
| `FileStorageService.FileStorageException` | **500** | `Could not store uploaded file` |

> **Quan trọng:** đăng nhập sai email **hoặc** sai mật khẩu đều trả **404** (`ResourceNotFoundException`), không phải 401. FE cần bắt 404 ở màn hình login.

### 1.4. Xác thực (JWT)

- Gửi access token qua header: `Authorization: Bearer <accessToken>`
- Token là JWT ký **HS256**, resource server tự decode (không có filter custom).
- **Claims trong token:** `userId` (number), `role` (`ADMIN` | `RECRUITER` | `CANDIDATE`), `type` (`ACCESS` | `REFRESH`), `jti`, `iat`, `exp`.
- **Thời hạn** (`application.yml`):
  - `accessToken`: **300 giây (5 phút)**
  - `refreshToken`: **10800 giây (3 giờ)**, được lưu DB và có thể bị revoke khi logout.
- Authority được sinh từ claim `role` với prefix `ROLE_` → `ROLE_ADMIN`, `ROLE_RECRUITER`, `ROLE_CANDIDATE`.

> Access token chỉ sống 5 phút → FE **bắt buộc** cài interceptor bắt lỗi 401 để tự gọi `POST /api/auth/refresh-token`.

### 1.5. Phân quyền theo URL (`SecurityConfig`)

| Pattern | Quyền |
|---|---|
| `/api/auth/register`, `/api/auth/register/recruiter`, `/api/auth/login`, `/api/auth/refresh-token` | Public |
| `/api/jobs/**`, `/api/files/**`, `/uploads/**`, `/api/companies/**`, `/` | Public |
| `/api/admin/**` | `ROLE_ADMIN` |
| `/api/recruiter/**` | `ROLE_RECRUITER` hoặc `ROLE_ADMIN` |
| `/api/applications/**` | `ROLE_CANDIDATE` |
| Còn lại (`/api/users`, `/api/candidates`, `/api/recruiters`, `/api/resumes`) | Cần đăng nhập (mọi role) |

> `POST /api/auth/logout` **không** nằm trong whitelist → phải gửi kèm `Authorization` header.

### 1.6. CORS

Chỉ cho phép origin: `http://localhost:3000` và `http://127.0.0.1:3000`.
Methods: `GET, POST, PUT, PATCH, DELETE, OPTIONS`. Headers: `*`. `allowCredentials: true`.

> FE chạy ở port khác 3000 sẽ bị chặn CORS — cần sửa `SecurityConfig#corsConfigurationSource`.

---
## 1.7. Ghi chú quan trọng cho Front-end

1. **Envelope hai lớp:** thành công → `{ message, data }`; lỗi → `{ timestamp, status, error, message, details }`. Hai cấu trúc khác nhau, interceptor cần phân biệt theo HTTP status.
2. **Ngày giờ là số (epoch seconds)**, không phải chuỗi ISO. Ví dụ: `createdAt: 1750598400.000000000` → `new Date(1750598400 * 1000)`.
3. **Access token chỉ 5 phút** → bắt buộc có cơ chế refresh tự động khi gặp `401` (body rỗng).
4. **Mọi `PATCH` đều là cập nhật toàn phần (behave như `PUT`)** — field không gửi sẽ bị set `null` hoặc `""`. Luôn merge dữ liệu cũ trước khi gửi.
5. **`fullname` viết thường** trong response; request nhận cả `fullname` và `fullName` (`@JsonAlias`).
6. **`avatar` (request) ↔ `avatarUrl` (response)** — tên field khác nhau. Tương tự **`companyId` (request) ↔ `company` object (response)**.
7. **Ảnh/file:** API chỉ trả **tên file**, FE tự ghép `{baseUrl}/uploads/{fileName}`.
8. **`candidate.id` = `recruiter.id` = `user.id`** (`@MapsId`) — không cần map riêng.
9. **`CandidateResponse` có field trùng lặp:** vừa có `user.email` vừa có `email` ở cấp ngoài (`fullname`, `phone` tương tự). `RecruiterResponse` **không** có.
10. **Login sai trả `404`**, không phải `401`.
11. **CORS chỉ mở cho `localhost:3000` / `127.0.0.1:3000`**.
12. **Đã có phân trang server-side** trên **mọi** endpoint list — response `data` giờ là **Spring `Page<T>`** (không phải mảng phẳng). FE bắt buộc đọc mảng qua `data.content` và tự truyền `?page=&size=&sort=`. Xem mục **1.8** để nắm quy ước phân trang chung.
13. **Thiếu kiểm tra quyền sở hữu** ở `/api/resumes/**`, `/api/recruiters/{id}`, `PATCH /api/users`, `PATCH /api/recruiter/jobs/{id}` — FE không nên để lộ id của user khác trên UI.
14. **Đăng ký không gửi `phone`** có thể gặp `409 Phone already exists` do hệ thống kiểm tra chuỗi rỗng đã tồn tại.

---

## 1.8. Phân trang (Pagination)

**Mọi endpoint trả về danh sách** đều đã bọc kết quả trong Spring `Page<T>`. FE truyền pagination qua **query string** và đọc metadata trong `data`.

### Query params dùng chung

| Param | Kiểu | Mặc định | Ghi chú |
|---|---|---|---|
| `page` | number | `0` | **Đánh số từ 0** — trang đầu tiên là `page=0`. `page=1` là trang thứ hai. |
| `size` | number | `10` | Số phần tử mỗi trang. **Tối đa 100** — vượt quá bị server clamp về 100 (không lỗi). |
| `sort` | string | tuỳ endpoint | `field,asc` hoặc `field,desc`. Ví dụ `sort=createdAt,desc`. Có thể lặp nhiều lần (`sort=status,asc&sort=id,desc`). |

**Ví dụ:** `GET /api/admin/users?page=2&size=20&sort=fullname,asc`

**Mặc định `sort` khác nhau theo nhóm:**
- Đơn ứng tuyển (`/api/applications`, `/api/recruiter/applications`, `/api/admin/applications`): mặc định `sort=appliedAt,desc` (mới nhất trước).
- Còn lại: mặc định `sort=id,asc`.

### Cấu trúc `data` khi phân trang — Spring `Page<T>`

```json
{
  "message": "success",
  "data": {
    "content": [ /* T[] — mảng phần tử trang hiện tại */ ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 10,
      "sort": { "sorted": true, "unsorted": false, "empty": false },
      "offset": 0,
      "paged": true,
      "unpaged": false
    },
    "totalElements": 42,
    "totalPages": 5,
    "last": false,
    "first": true,
    "number": 0,
    "numberOfElements": 10,
    "size": 10,
    "sort": { "sorted": true, "unsorted": false, "empty": false },
    "empty": false
  }
}
```

Field FE cần chú ý:

| Field | Kiểu | Ý nghĩa |
|---|---|---|
| `data.content` | `T[]` | **Danh sách phần tử** trang hiện tại — thay cho mảng phẳng trước đây |
| `data.totalElements` | number | Tổng số phần tử trên toàn bộ dataset (đã áp filter) |
| `data.totalPages` | number | Tổng số trang |
| `data.number` | number | Chỉ số trang hiện tại (0-based) — trùng với `page` FE gửi lên |
| `data.size` | number | Số phần tử mỗi trang (server đã clamp `≤ 100`) |
| `data.numberOfElements` | number | Số phần tử **thực tế** ở trang hiện tại (trang cuối có thể `< size`) |
| `data.first` / `data.last` | boolean | Có phải trang đầu/cuối không |
| `data.empty` | boolean | Trang hiện tại có phần tử nào không |

> ⚠️ **Breaking change so với version trước:** trước đây `data` là `T[]` (mảng phẳng), bây giờ là object `Page<T>`. FE **bắt buộc** phải đổi từ `response.data.data` sang `response.data.data.content` để lấy mảng.
> ⚠️ Nếu FE **không truyền** `page`/`size`/`sort`, server tự áp mặc định (`page=0`, `size=10`, `sort` theo nhóm ở trên) — vẫn trả `Page<T>`, không quay về mảng phẳng.

### Endpoint được phân trang

Bảng dưới liệt kê **toàn bộ** endpoint list (đã phân trang server-side). Các endpoint trả **một object** (chi tiết theo id, profile…) **không** phân trang và giữ nguyên định dạng cũ.

| # | Method | Path | `sort` mặc định |
|---:|---|---|---|
| 10 | GET | `/api/jobs` | `id,asc` |
| 11 | GET | `/api/jobs/search?title=` | `id,asc` |
| 13 | GET | `/api/companies` | `id,asc` |
| 14 | GET | `/api/companies/search?name=` | `id,asc` |
| 17 | GET | `/api/candidates` | `id,asc` |
| 21 | GET | `/api/resumes` | `id,asc` |
| 23 | GET | `/api/resumes/candidate/{candidateId}` | `id,asc` |
| 26 | GET | `/api/recruiters` | `id,asc` |
| 30 | GET | `/api/recruiter/jobs` | `id,asc` |
| 31 | GET | `/api/recruiter/jobs/search?title=` | `id,asc` |
| 36 | GET | `/api/admin/jobs` | `id,asc` |
| 37 | GET | `/api/admin/jobs/search?title=` | `id,asc` |
| 42 | GET | `/api/admin/companies` | `id,asc` |
| 43 | GET | `/api/admin/companies/active` | `id,asc` |
| 49 | GET | `/api/applications` | `appliedAt,desc` |
| 52 | GET | `/api/recruiter/applications` | `appliedAt,desc` |
| 53 | GET | `/api/recruiter/applications/job/{jobId}` | `appliedAt,desc` |
| 56 | GET | `/api/admin/applications` | `appliedAt,desc` |
| 58 | GET | `/api/admin/applications/job/{jobId}` | `appliedAt,desc` |
| 59 | GET | `/api/admin/applications/candidate/{candidateId}` | `appliedAt,desc` |
| 62 | GET | `/api/admin/users` | `id,asc` |
| — | GET | `/api/admin/users/search` | `id,asc` |

> ⚠️ `sort` truyền lên phải là **tên field của entity JPA** (ví dụ `appliedAt`, `createdAt`, `fullname`), **không** phải tên cột SQL. Sai tên field → `500 PropertyReferenceException`.

---

## 2. Enum dùng chung

| Enum | Giá trị |
|---|---|
| `Role` | `ADMIN`, `RECRUITER`, `CANDIDATE` |
| `JobStatus` | `OPEN`, `PAUSED`, `CLOSED` |
| `EmploymentType` | `FULLTIME`, `PARTTIME` |
| `ApplicationStatus` | `APPLICATION_CREATED`, `SCREENING`, `INTERVIEW`, `OFFER`, `HIRED`, `REJECTED`, `WITHDRAWN` |

> Gửi giá trị enum sai (ví dụ `"FULL_TIME"`) → **400** với message `Malformed request or unsupported enum value`.
> `ApplicationStatus` không đổi tuỳ tiện được — xem luồng chuyển trạng thái ở mục 16.1.

---

## 3. Các schema response dùng lại

### `UserResponse`

| Field | Kiểu | Ghi chú |
|---|---|---|
| `id` | number | |
| `email` | string | |
| `fullname` | string | **Chú ý:** viết thường chữ `n` |
| `phone` | string \| null | |
| `avatarUrl` | string | Tên file ảnh, mặc định `"default-avatar.png"`. FE ghép: `{baseUrl}/uploads/{avatarUrl}` |
| `active` | boolean | Tài khoản còn hoạt động không. `false` → **không đăng nhập được** (mục 17) |
| `role` | `Role` | |

> `active` xuất hiện ở **mọi nơi có `UserResponse`**: `LoginResponse.user`, response đăng ký, `CandidateResponse.user`, `RecruiterResponse.user`.
> **Chú ý tên field:** ở đây là `active`, còn `CompanyResponse` dùng `isActive` — hai module đặt tên khác nhau.

### `CompanyResponse`

| Field | Kiểu | Ghi chú |
|---|---|---|
| `id` | number | |
| `name` | string | |
| `logo` | string \| null | Tên file → `{baseUrl}/uploads/{logo}` |
| `email` | string | |
| `website` | string \| null | |
| `description` | string | |
| `address` | string | |
| `createdAt` | number | epoch seconds |
| `updatedAt` | number \| null | |
| `isActive` | boolean | |

### `CandidateResponse`

| Field | Kiểu | Ghi chú |
|---|---|---|
| `id` | number | **Trùng với `user.id`** (`@MapsId`) |
| `user` | `UserResponse` \| null | |
| `linkedinUrl` | string \| null | |
| `githubUrl` | string \| null | |
| `portfolioUrl` | string \| null | |
| `currentPosition` | string \| null | |
| `yearsOfExperience` | number \| null | |
| `createdAt` | number | |
| `updatedAt` | number \| null | |
| `email` | string \| null | **Field phẳng bonus** — copy từ `user.email` |
| `fullname` | string \| null | **Field phẳng bonus** — copy từ `user.fullname` |
| `phone` | string \| null | **Field phẳng bonus** — copy từ `user.phone` |

> `CandidateResponse` có thêm 3 getter tiện ích nên JSON trả về **vừa có** object `user` **vừa có** `email` / `fullname` / `phone` ở cấp ngoài cùng. FE có thể dùng cách nào cũng được; nếu `user` là `null` thì 3 field này cũng `null`.

### `RecruiterResponse`

| Field | Kiểu | Ghi chú |
|---|---|---|
| `id` | number | **Trùng với `user.id`** (`@MapsId`) |
| `user` | `UserResponse` \| null | |
| `company` | `CompanyResponse` \| null | `null` nếu recruiter chưa gắn công ty |
| `position` | string \| null | |

> Khác `CandidateResponse`, ở đây **không có** field phẳng `email`/`fullname`/`phone` và **không có** `createdAt`/`updatedAt`.

### `JobResponse`

| Field | Kiểu | Ghi chú |
|---|---|---|
| `id` | number | |
| `title` | string | |
| `description` | string \| null | |
| `requirements` | string \| null | |
| `location` | string \| null | |
| `employmentType` | `EmploymentType` \| null | |
| `company` | `CompanyResponse` \| null | Object lồng, **không phải** `companyId` |
| `salaryMin` | number \| null | |
| `salaryMax` | number \| null | |
| `status` | `JobStatus` | |
| `createdBy` | number \| null | `userId` của người tạo |
| `createdAt` | number | |
| `updatedAt` | number \| null | |

> **Bất đối xứng request/response:** request gửi lên `companyId` (number), response trả về `company` (object).

### `ResumeResponse`

| Field | Kiểu |
|---|---|
| `id` | number |
| `candidateId` | number |
| `fileName` | string |
| `fileUrl` | string |
| `createdAt` | number |

### `JobApplicationResponse`

| Field | Kiểu |
|---|---|
| `id` | number |
| `candidateId` | number |
| `jobId` | number |
| `resumeId` | number \| null |
| `status` | `ApplicationStatus` |
| `source` | string \| null |
| `expectedSalary` | number \| null |
| `note` | string \| null |
| `appliedAt` | number |
| `updatedAt` | number \| null |

> Response **phẳng** — chỉ trả id, không lồng object `job`/`candidate`/`resume`. FE cần chi tiết job thì gọi thêm `GET /api/jobs/{jobId}`.

### `LoginResponse`

| Field | Kiểu |
|---|---|
| `user` | `UserResponse` |
| `accessToken` | string |
| `refreshToken` | string |

### `AccessTokenResponse`

| Field | Kiểu |
|---|---|
| `accessToken` | string |

---

## 4. Bảng tổng hợp endpoint

Đã tách sang file riêng, nhóm theo controller/tính năng: **[GROUP_API.md](GROUP_API.md)**.

---

## 5. Auth API — `/api/auth`

### 5.1. `POST /api/auth/register` — Đăng ký ứng viên

**Quyền:** Public · **Success:** `201 Created`

**Request body — `CandidateRequest` (kế thừa `UserRequest`):**

| Field | Kiểu | Bắt buộc | Validation |
|---|---|:---:|---|
| `fullname` | string | ✅ | Không rỗng, ≤ 100 ký tự. Nhận cả alias `fullName` |
| `email` | string | ✅ | Không rỗng, đúng định dạng email, ≤ 150 ký tự |
| `phone` | string | ❌ | ≤ 20 ký tự |
| `password` | string | ❌ | Nếu có: **≥ 6 ký tự** (message ghi nhầm là 8). Nếu bỏ trống → server tự đặt `"password123"` |
| `avatar` | string | ❌ | Tên file ảnh (lấy từ `POST /api/files/upload`). **Bị bỏ qua khi tạo mới** — luôn set `default-avatar.png` |
| `linkedinUrl` | string | ❌ | ≤ 255 ký tự |
| `githubUrl` | string | ❌ | ≤ 255 ký tự |
| `portfolioUrl` | string | ❌ | ≤ 255 ký tự |
| `currentPosition` | string | ❌ | ≤ 100 ký tự |
| `yearsOfExperience` | number | ❌ | ≥ 0 |

**Ví dụ request:**

```json
{
  "fullname": "Nguyen Van A",
  "email": "a.nguyen@example.com",
  "phone": "0900000001",
  "password": "secret123",
  "linkedinUrl": "https://linkedin.com/in/a-nguyen",
  "currentPosition": "Java Developer",
  "yearsOfExperience": 3
}
```

**Ví dụ response `201`:**

```json
{
  "message": "create success",
  "data": {
    "id": 12,
    "user": {
      "id": 12,
      "email": "a.nguyen@example.com",
      "fullname": "Nguyen Van A",
      "phone": "0900000001",
      "avatarUrl": "default-avatar.png",
      "role": "CANDIDATE"
    },
    "linkedinUrl": "https://linkedin.com/in/a-nguyen",
    "githubUrl": null,
    "portfolioUrl": null,
    "currentPosition": "Java Developer",
    "yearsOfExperience": 3,
    "createdAt": 1750598400.000000000,
    "updatedAt": null,
    "email": "a.nguyen@example.com",
    "fullname": "Nguyen Van A",
    "phone": "0900000001"
  }
}
```

**Lỗi nghiệp vụ (`409 Conflict`):**

| `message` | Điều kiện |
|---|---|
| `Email and password cannot be empty` | Email rỗng |
| `Email must start with a letter and contain a valid domain` | Email không khớp regex `^[A-Za-z][A-Za-z0-9._%+-]*@[A-Za-z0-9-]+(?:\.[A-Za-z0-9-]+)+$` (email bắt đầu bằng số/ký tự đặc biệt sẽ bị chặn dù `@Email` cho qua) |
| `Email already exists` | Email đã tồn tại |
| `Phone already exists` | Số điện thoại đã tồn tại. **Cảnh báo:** nếu không gửi `phone`, hệ thống kiểm tra `existsByPhone("")` → user thứ hai không gửi phone có thể bị 409 |

---

### 5.2. `POST /api/auth/register/recruiter` — Đăng ký nhà tuyển dụng

**Quyền:** Public · **Success:** `201 Created`

**Request body — `RecruiterRequest` (kế thừa `UserRequest`):**

| Field | Kiểu | Bắt buộc | Validation |
|---|---|:---:|---|
| `fullname`, `email`, `phone`, `password`, `avatar` | | | Giống `UserRequest` ở mục 5.1 |
| `companyId` | number | ❌ | ID công ty. `null` → recruiter chưa gắn công ty |
| `position` | string | ❌ | ≤ 255 ký tự |

**Ví dụ request:**

```json
{
  "fullname": "Tran Thi B",
  "email": "b.tran@example.com",
  "phone": "0900000002",
  "password": "secret123",
  "companyId": 1,
  "position": "HR Manager"
}
```

**Ví dụ response `201`:**

```json
{
  "message": "create success",
  "data": {
    "id": 13,
    "user": {
      "id": 13,
      "email": "b.tran@example.com",
      "fullname": "Tran Thi B",
      "phone": "0900000002",
      "avatarUrl": "default-avatar.png",
      "role": "RECRUITER"
    },
    "company": {
      "id": 1,
      "name": "FPT Software",
      "logo": "logo.png",
      "email": "contact@fpt.com",
      "website": "https://fpt.com",
      "description": "Công ty phần mềm",
      "address": "Hà Nội",
      "createdAt": 1750598400.000000000,
      "updatedAt": null,
      "isActive": true
    },
    "position": "HR Manager"
  }
}
```

**Lỗi:** giống mục 5.1, thêm `404` `Company not found` nếu `companyId` không tồn tại.

---

### 5.3. `POST /api/auth/login` — Đăng nhập

**Quyền:** Public · **Success:** `200 OK`

**Request body — `LoginRequest`:**

| Field | Kiểu | Bắt buộc |
|---|---|:---:|
| `email` | string | ✅ |
| `password` | string | ✅ |

> DTO này **không có Bean Validation** → sai/thiếu field sẽ báo lỗi ở tầng nghiệp vụ (409/500) chứ không phải 400.

```json
{ "email": "a.nguyen@example.com", "password": "secret123" }
```

**Response `200`:**

```json
{
  "message": "login success",
  "data": {
    "user": {
      "id": 12,
      "email": "a.nguyen@example.com",
      "fullname": "Nguyen Van A",
      "phone": "0900000001",
      "avatarUrl": "default-avatar.png",
      "role": "CANDIDATE"
    },
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
  }
}
```

**Lỗi:**

| Status | `message` | Điều kiện |
|---:|---|---|
| 404 | `user not found` | Email không tồn tại |
| 404 | `email or password incorrect` | Sai mật khẩu |
| 409 | `email or password not empty` | Email hoặc password là chuỗi rỗng |
| 409 | `User is inactive` | Tài khoản bị khoá (`active = false`) |

> FE nên gộp 404 (`user not found` / `email or password incorrect`) thành một thông báo chung "Email hoặc mật khẩu không đúng".

---

### 5.4. `POST /api/auth/refresh-token` — Cấp lại access token

**Quyền:** Public · **Success:** `200 OK`

**Request body — `RefreshTokenRequest`:**

| Field | Kiểu | Bắt buộc | Validation |
|---|---|:---:|---|
| `refreshToken` | string | ✅ | `@NotBlank` → `refreshToken is required` |

```json
{ "refreshToken": "eyJhbGciOiJIUzI1NiJ9..." }
```

**Response `200`:**

```json
{
  "message": "refresh token success",
  "data": { "accessToken": "eyJhbGciOiJIUzI1NiJ9..." }
}
```

**Lỗi:**

| Status | `message` | Điều kiện |
|---:|---|---|
| 400 | `Request validation failed` | `refreshToken` rỗng |
| 409 | `Invalid refresh token` | Token sai chữ ký, hết hạn, sai `type`, hoặc đã bị revoke/không còn active trong DB |
| 409 | `User is inactive` | Tài khoản bị khoá |
| 404 | `User not found with id: {id}` | User trong token không còn tồn tại |

> Endpoint này **chỉ trả access token mới**, refresh token cũ vẫn giữ nguyên (không xoay vòng).

---

### 5.5. `POST /api/auth/logout` — Thu hồi refresh token

**Quyền:** **Cần `Authorization: Bearer <accessToken>`** · **Success:** `200 OK`

**Request body:** `RefreshTokenRequest` (giống 5.4)

**Response `200`:**

```json
{ "message": "logout success", "data": null }
```

**Lỗi:**

| Status | `message` | Điều kiện |
|---:|---|---|
| 401 | *(body rỗng)* | Thiếu/sai access token |
| 400 | `Request validation failed` | `refreshToken` rỗng |
| 409 | `Invalid refresh token` | Token không hợp lệ hoặc đã bị revoke trước đó |

---

## 6. User API — `/api/users`

### 6.1. `PATCH /api/users/password` — Đổi mật khẩu

**Quyền:** Cần đăng nhập · **Success:** `200 OK` · **User đích:** lấy từ claim `userId` trong access token.

**Request body — `ChangePasswordRequest`:**

| Field | Kiểu | Bắt buộc | Alias JSON được chấp nhận |
|---|---|:---:|---|
| `oldPassword` | string | ✅ | `oldpassword`, `old_password`, `currentPassword`, `currentpassword`, `current_password` |
| `newPassword` | string | ✅ | `newpassword`, `new_password` |
| `confirmPassword` | string | ✅ | `confirmpassword`, `confirm_password` |

```json
{
  "oldPassword": "secret123",
  "newPassword": "newSecret456",
  "confirmPassword": "newSecret456"
}
```

**Response `200`:**

```json
{ "message": "success", "data": null }
```

**Lỗi:**

| Status | `message` |
|---:|---|
| 400 | `Request validation failed` — `old password is required` / `new password is required` / `confirm password is required` |
| 409 | `Old password does not match` |
| 409 | `New password must be different from old password` |
| 409 | `Confirm password does not match` |
| 401 | *(body rỗng)* — chưa đăng nhập |

> Không có ràng buộc độ dài cho `newPassword` ở endpoint này — FE nên tự validate.

---

### 6.2. `PATCH /api/users` — Cập nhật thông tin user

**Quyền:** Cần đăng nhập · **Success:** `200 OK`

**Request body — `UserRequest`:**

| Field | Kiểu | Bắt buộc | Ghi chú |
|---|---|:---:|---|
| `fullname` | string | ✅ | ≤ 100 ký tự. Alias: `fullName` |
| `email` | string | ✅ | **Dùng để XÁC ĐỊNH user cần update** — không phải để đổi email |
| `phone` | string | ❌ | ≤ 20 ký tự |
| `password` | string | ❌ | **Bị bỏ qua** — không dùng để đổi mật khẩu (dùng `PATCH /api/users/password`) |
| `avatar` | string | ❌ | Nếu khác `null`/rỗng thì cập nhật `avatarUrl` |

> ⚠️ **Lưu ý quan trọng cho FE:** endpoint này tìm user theo `email` **trong body**, **không** theo token. Body `email` bắt buộc phải là email của chính user đang đăng nhập; endpoint hiện **không kiểm tra** quyền sở hữu. Ngoài ra `fullname` và `phone` luôn bị ghi đè (thiếu → thành chuỗi rỗng) nên **phải gửi đủ toàn bộ field**.

```json
{
  "fullname": "Nguyen Van A",
  "email": "a.nguyen@example.com",
  "phone": "0911111111",
  "avatar": "b3f1c2d4-....png"
}
```

**Response `200`:** `data` là `UserResponse`.

```json
{
  "message": "success",
  "data": {
    "id": 12,
    "email": "a.nguyen@example.com",
    "fullname": "Nguyen Van A",
    "phone": "0911111111",
    "avatarUrl": "b3f1c2d4-....png",
    "role": "CANDIDATE"
  }
}
```

**Lỗi:** `400` validation · `404` `user not found` (email không tồn tại) · `401` chưa đăng nhập.

---

## 7. File API — `/api/files` và `/uploads`

### 7.1. `POST /api/files/upload` — Upload file

**Quyền:** Public · **Success:** `201 Created` · **Content-Type:** `multipart/form-data`

| Form field | Kiểu | Bắt buộc |
|---|---|:---:|
| `file` | binary | ✅ (tên field **phải** là `file`) |

**Giới hạn:** `max-file-size` = `max-request-size` = **10MB** (env `MAX_UPLOAD_SIZE`).

**Response `201`:** `data` là **tên file đã lưu** (string), dạng `{uuid}{phần mở rộng}`.

```json
{
  "message": "create file success",
  "data": "b3f1c2d4-9a77-4e2f-9d3c-1a2b3c4d5e6f.pdf"
}
```

> - File rỗng → `data` là chuỗi rỗng `""` và vẫn trả `201`. FE nên tự chặn.
> - Không giới hạn loại file (mọi extension đều được chấp nhận).
> - Dùng giá trị `data` này gán cho `avatar` (user), `logo` (company), `fileUrl`/`fileName` (resume).

**Lỗi:** `500` `Could not store uploaded file` · `400` `Invalid file name`.

**Ví dụ (FE):**

```js
const form = new FormData();
form.append("file", fileInput.files[0]);
const res = await fetch(`${BASE_URL}/api/files/upload`, { method: "POST", body: form });
const { data: storedName } = await res.json();
// URL hiển thị: `${BASE_URL}/uploads/${storedName}`
```

### 7.2. `GET /uploads/{fileName}` — Tải file tĩnh

**Quyền:** Public · Trả về file nhị phân (không phải JSON), phục vụ từ thư mục `ats.upload.directory` (mặc định `D:/uploads`). Không tìm thấy → `404`.

---

## 8. Job API (Public / Ứng viên) — `/api/jobs`

Toàn bộ nhóm này **không cần token**, và **chỉ trả job có `status != CLOSED`** (tức `OPEN` hoặc `PAUSED`).

### 8.1. `GET /api/jobs` — Danh sách job chưa đóng

**📄 Đã phân trang.** Query params: `page` (mặc định `0`), `size` (mặc định `10`, tối đa `100`), `sort` (mặc định `id,asc`) — xem mục **1.8**.

**Response `200`:** `data` là `Page<JobResponse>` — mảng phần tử nằm trong `data.content`.

```json
{
  "message": "success",
  "data": {
    "content": [
      {
        "id": 5,
        "title": "Java Backend Developer",
        "description": "Phát triển hệ thống backend",
        "requirements": "3+ năm Spring Boot",
        "location": "Hà Nội",
        "employmentType": "FULLTIME",
        "company": {
          "id": 1,
          "name": "FPT Software",
          "logo": "logo.png",
          "email": "contact@fpt.com",
          "website": "https://fpt.com",
          "description": "Công ty phần mềm",
          "address": "Hà Nội",
          "createdAt": 1750598400.000000000,
          "updatedAt": null,
          "isActive": true
        },
        "salaryMin": 20000000,
        "salaryMax": 35000000,
        "status": "OPEN",
        "createdBy": 13,
        "createdAt": 1750598400.000000000,
        "updatedAt": null
      }
    ],
    "totalElements": 42,
    "totalPages": 5,
    "number": 0,
    "size": 10,
    "numberOfElements": 1,
    "first": true,
    "last": false,
    "empty": false
  }
}
```

### 8.2. `GET /api/jobs/search?title={title}` — Tìm job theo tiêu đề

**📄 Đã phân trang.** Query bổ sung `page`, `size`, `sort` — xem mục **1.8**.

| Query param | Kiểu | Bắt buộc | Ghi chú |
|---|---|:---:|---|
| `title` | string | ✅ | Tìm gần đúng (`LIKE`), loại trừ job `CLOSED` |
| `page`, `size`, `sort` | | ❌ | Mặc định `page=0`, `size=10`, `sort=id,asc` |

> Thiếu `title` → `400` (`MissingServletRequestParameterException`, body mặc định của Spring, **không** theo `ApiError`).

**Response `200`:** `Page<JobResponse>` (mảng rỗng ở `data.content` nếu không khớp).

### 8.3. `GET /api/jobs/{id}` — Chi tiết job

| Path param | Kiểu |
|---|---|
| `id` | number |

**Response `200`:** `JobResponse`.

**Lỗi:** `404` `Job not found` — khi id không tồn tại **hoặc** job có `status = CLOSED` (cố tình trả 404 để không lộ sự tồn tại của job đã đóng).

---

## 9. Company API (Public / Ứng viên) — `/api/companies`

Nhóm này dành cho ứng viên và trang public, **không cần token**, và **chỉ trả về công ty có `isActive = true`**.
Công ty đã bị ngưng hoạt động (`isActive = false`) hoàn toàn ẩn khỏi nhóm này — muốn xem cần dùng nhóm admin (mục 15).

### 9.1. `GET /api/companies` — Danh sách công ty đang hoạt động

**Quyền:** Public · **Success:** `200 OK` · **📄 Đã phân trang** (`page`, `size`, `sort` — xem mục 1.8, mặc định `sort=id,asc`)

**Response `200`:** `data` là `Page<CompanyResponse>` — mảng phần tử ở `data.content` (rỗng nếu chưa có công ty nào hoạt động).

```json
{
  "message": "success",
  "data": {
    "content": [
      {
        "id": 1,
        "name": "FPT Software",
        "logo": "b3f1c2d4-9a77-4e2f-9d3c-1a2b3c4d5e6f.png",
        "email": "contact@fpt.com",
        "website": "https://fpt.com",
        "description": "Công ty phần mềm hàng đầu",
        "address": "Hà Nội",
        "createdAt": 1750598400.000000000,
        "updatedAt": null,
        "isActive": true
      }
    ],
    "totalElements": 1,
    "totalPages": 1,
    "number": 0,
    "size": 10,
    "first": true,
    "last": true,
    "empty": false
  }
}
```

> Mọi phần tử trả về luôn có `isActive: true`.

### 9.2. `GET /api/companies/search?name={name}` — Tìm công ty theo tên

**Quyền:** Public · **Success:** `200 OK`

| Query param | Kiểu | Bắt buộc | Ghi chú |
|---|---|:---:|---|
| `name` | string | ✅ | Tìm **gần đúng, không phân biệt hoa thường** (`LIKE name%`), chỉ trong các công ty `isActive = true` |

**Ví dụ:** `GET /api/companies/search?name=fpt` → khớp `"FPT Software"`, `"Fpt Telecom"`.

**📄 Đã phân trang** (`page`, `size`, `sort` — xem mục 1.8, mặc định `sort=id,asc`).

**Response `200`:** `data` là `Page<CompanyResponse>` (cấu trúc trong `data.content` giống mục 9.1). Không khớp → `data.content = []`, `data.empty = true` (**không** phải `404`).

**Lỗi:**

| Status | Điều kiện |
|---:|---|
| 400 | Thiếu param `name` (`MissingServletRequestParameterException` — body mặc định của Spring, **không** theo `ApiError`) |

> Truyền `name=` (chuỗi rỗng) là hợp lệ và khớp **mọi** công ty đang hoạt động (`LIKE %%`) — FE nên chặn gọi khi ô tìm kiếm trống, hoặc dùng `GET /api/companies` thay thế.

### 9.3. `GET /api/companies/{id}` — Chi tiết công ty

**Quyền:** Public · **Success:** `200 OK`

| Path param | Kiểu | Ghi chú |
|---|---|---|
| `id` | number | ID công ty |

**Response `200`:** `data` là `CompanyResponse`.

```json
{
  "message": "success",
  "data": {
    "id": 1,
    "name": "FPT Software",
    "logo": "b3f1c2d4-9a77-4e2f-9d3c-1a2b3c4d5e6f.png",
    "email": "contact@fpt.com",
    "website": "https://fpt.com",
    "description": "Công ty phần mềm hàng đầu",
    "address": "Hà Nội",
    "createdAt": 1750598400.000000000,
    "updatedAt": null,
    "isActive": true
  }
}
```

**Lỗi:**

| Status | `message` | Điều kiện |
|---:|---|---|
| 404 | `Company not found` | ID không tồn tại **hoặc** công ty có `isActive = false` |
| 400 | *(body mặc định Spring)* | `id` không phải số (ví dụ `/api/companies/abc`) |

> Giống cách `GET /api/jobs/{id}` xử lý job `CLOSED`: công ty đã ngưng hoạt động trả **404** thay vì 403, để không lộ sự tồn tại của bản ghi.

---

## 10. Candidate API — `/api/candidates`

**Quyền:** Cần đăng nhập (mọi role).

### 10.1. `POST /api/candidates` — Tạo ứng viên

**Success:** `201 Created` · Request/response **giống hệt** `POST /api/auth/register` (mục 5.1), khác là endpoint này yêu cầu token. Dùng cho admin tạo ứng viên từ trang quản trị.

### 10.2. `GET /api/candidates` — Danh sách ứng viên

**📄 Đã phân trang** (`page`, `size`, `sort` — xem mục 1.8, mặc định `sort=id,asc`).

**Response `200`:** `data` là `Page<CandidateResponse>` — mảng ở `data.content` (mỗi phần tử đã kèm `user`).

### 10.3. `GET /api/candidates/profile` — Hồ sơ ứng viên đang đăng nhập

Lấy `userId` từ token (candidate dùng chung khoá chính với user).

**Response `200`:** `CandidateResponse`.

**Lỗi:** `404` `Candidate not found` — nếu user đang đăng nhập không phải ứng viên (ví dụ recruiter/admin gọi endpoint này) · `401` chưa đăng nhập.

### 10.4. `PATCH /api/candidates` — Cập nhật hồ sơ ứng viên đang đăng nhập

**Request body:** `CandidateRequest` (giống mục 5.1).

> ⚠️ Đây là **cập nhật toàn phần**: mọi field trong `CandidateRequest` đều bị ghi đè, field không gửi sẽ thành `null` (riêng `fullname`/`phone` thành chuỗi rỗng). FE **phải gửi đủ toàn bộ dữ liệu hồ sơ**, kể cả field không thay đổi.
> - `email` trong body **không** đổi được email tài khoản.
> - `avatar` chỉ được cập nhật khi khác `null` và khác rỗng.
> - `password` bị bỏ qua.

**Response `200`:** `CandidateResponse`.

**Lỗi:** `400` validation · `404` `Candidate not found` · `401`.

---

## 11. Resume API — `/api/resumes`

**Quyền:** Cần đăng nhập (mọi role).

> ⚠️ Các endpoint dưới đây **không kiểm tra quyền sở hữu**: `candidateId` lấy từ body/path chứ không từ token, nên bất kỳ user đăng nhập nào cũng đọc/sửa/xoá được CV của người khác. FE cần tự giới hạn theo `candidateId` của user hiện tại.

### 11.1. `POST /api/resumes` — Tạo CV

**Success:** `201 Created`

**Request body — `ResumeRequest`:**

| Field | Kiểu | Bắt buộc | Validation |
|---|---|:---:|---|
| `candidateId` | number | ✅ | `Candidate id is required` |
| `fileName` | string | ✅ | `File name is required` — tên hiển thị cho người dùng |
| `fileUrl` | string | ✅ | `File url is required` — tên file trả về từ `POST /api/files/upload` |

```json
{
  "candidateId": 12,
  "fileName": "CV_NguyenVanA.pdf",
  "fileUrl": "b3f1c2d4-9a77-4e2f-9d3c-1a2b3c4d5e6f.pdf"
}
```

**Response `201`:**

```json
{
  "message": "create success",
  "data": {
    "id": 7,
    "candidateId": 12,
    "fileName": "CV_NguyenVanA.pdf",
    "fileUrl": "b3f1c2d4-9a77-4e2f-9d3c-1a2b3c4d5e6f.pdf",
    "createdAt": 1750598400.000000000
  }
}
```

**Lỗi:** `400` validation · `404` `Candidate not found`.

### 11.2. `GET /api/resumes` — Danh sách tất cả CV

**📄 Đã phân trang** (`page`, `size`, `sort` — xem mục 1.8, mặc định `sort=id,asc`).

**Response `200`:** `Page<ResumeResponse>` — mảng ở `data.content`.

### 11.3. `GET /api/resumes/{id}` — Chi tiết CV

**Response `200`:** `ResumeResponse` · **Lỗi:** `404` `Resume not found`.

### 11.4. `GET /api/resumes/candidate/{candidateId}` — CV theo ứng viên

**📄 Đã phân trang** (`page`, `size`, `sort` — xem mục 1.8, mặc định `sort=id,asc`).

**Response `200`:** `Page<ResumeResponse>` — mảng ở `data.content` · **Lỗi:** `404` `Candidate not found`.

### 11.5. `PATCH /api/resumes/{id}` — Cập nhật CV

**Request body:** `ResumeRequest` (cập nhật toàn phần — phải gửi đủ 3 field).

**Response `200`:** `ResumeResponse` · **Lỗi:** `400` validation · `404` `Resume not found` / `Candidate not found`.

### 11.6. `DELETE /api/resumes/{id}` — Xoá CV

**Response `200`:**

```json
{ "message": "delete success", "data": null }
```

**Lỗi:** `404` `Resume not found`.

---

## 12. Recruiter API — `/api/recruiters`

**Quyền:** Cần đăng nhập (mọi role — **không** giới hạn `RECRUITER` vì path không khớp pattern `/api/recruiter/**`).

### 12.1. `GET /api/recruiters` — Danh sách recruiter

**📄 Đã phân trang** (`page`, `size`, `sort` — xem mục 1.8, mặc định `sort=id,asc`).

**Response `200`:** `Page<RecruiterResponse>` — mảng ở `data.content` (mỗi phần tử kèm `user` và `company`).

### 12.2. `GET /api/recruiters/profile` — Hồ sơ recruiter đang đăng nhập

Lấy `userId` từ token (recruiter dùng chung khoá chính với user).

**Response `200`:** `RecruiterResponse` · **Lỗi:** `404` `Recruiter not found` nếu user không phải recruiter.

### 12.3. `PATCH /api/recruiters/{id}` — Cập nhật recruiter

| Path param | Kiểu | Ghi chú |
|---|---|---|
| `id` | number | Chính là `userId` của recruiter |

**Request body:** `RecruiterRequest` (mục 5.2) — **cập nhật toàn phần**, `companyId`/`position` không gửi sẽ bị set `null`.

**Response `200`:** `RecruiterResponse` · **Lỗi:** `400` validation · `404` `Recruiter not found` / `Company not found`.

> ⚠️ Không kiểm tra quyền sở hữu — id lấy từ path, FE nên chỉ truyền id của chính user đang đăng nhập.

---

## 13. Recruiter Job API — `/api/recruiter/jobs`

**Quyền:** `ROLE_RECRUITER` hoặc `ROLE_ADMIN`.

### 13.1. `POST /api/recruiter/jobs` — Tạo job

**Success:** `201 Created`

**Request body — `JobRequest`:**

| Field | Kiểu | Bắt buộc | Validation / Ghi chú |
|---|---|:---:|---|
| `title` | string | ✅ | Không rỗng, ≤ 150 ký tự |
| `description` | string | ❌ | Không giới hạn độ dài ở tầng DTO |
| `requirements` | string | ❌ | |
| `location` | string | ❌ | ≤ 100 ký tự |
| `employmentType` | `EmploymentType` | ❌ | `FULLTIME` \| `PARTTIME` |
| `companyId` | number | ✅ | `Company is required` |
| `salaryMin` | number | ❌ | ≥ 0 |
| `salaryMax` | number | ❌ | ≥ 0 (**không** có ràng buộc `salaryMax >= salaryMin`) |
| `status` | `JobStatus` | ❌ | Mặc định `OPEN` khi tạo mới |
| `createdBy` | number | ❌ | **FE không cần gửi** — server luôn ghi đè bằng `userId` từ token |

```json
{
  "title": "Java Backend Developer",
  "description": "Phát triển hệ thống backend",
  "requirements": "3+ năm Spring Boot",
  "location": "Hà Nội",
  "employmentType": "FULLTIME",
  "companyId": 1,
  "salaryMin": 20000000,
  "salaryMax": 35000000
}
```

**Response `201`:** `JobResponse` (xem mẫu ở mục 8.1, `message` = `"create success"`).

**Lỗi:** `400` validation / enum sai · `404` `Company not found with id: {id}` · `403` sai role.

### 13.2. `GET /api/recruiter/jobs` — Job do mình tạo

**📄 Đã phân trang** (`page`, `size`, `sort` — xem mục 1.8, mặc định `sort=id,asc`).

Lọc theo `createdBy = userId` trong token. **Response `200`:** `Page<JobResponse>` — mảng ở `data.content` (gồm cả job `CLOSED`).

### 13.3. `GET /api/recruiter/jobs/search?title={title}` — Tìm job của mình

**📄 Đã phân trang** (`page`, `size`, `sort` bổ sung — xem mục 1.8).

| Query param | Kiểu | Bắt buộc |
|---|---|:---:|
| `title` | string | ✅ |
| `page`, `size`, `sort` | | ❌ |

Lọc theo `title LIKE` **và** `createdBy = userId` trong token. **Response `200`:** `Page<JobResponse>` — mảng ở `data.content`.

### 13.4. `PATCH /api/recruiter/jobs/{id}` — Cập nhật job

**Request body:** `JobRequest`.

> ⚠️ **Cập nhật toàn phần** — mọi field đều bị ghi đè, field không gửi thành `null`. Ngoại lệ: `status` chỉ đổi khi gửi khác `null`; `createdBy` không thay đổi.
> ⚠️ **Không kiểm tra quyền sở hữu**: recruiter có thể sửa job của recruiter khác nếu biết `id`.

**Response `200`:** `JobResponse` · **Lỗi:** `400` validation · `404` `Job not found` / `Company not found with id: {id}`.

### 13.5. `PATCH /api/recruiter/jobs/{id}/active` — Mở job

Đặt `status = OPEN`. **Response `200`:** `JobResponse` · **Lỗi:** `404` `Job not found`.

### 13.6. `PATCH /api/recruiter/jobs/{id}/deactivate` — Đóng job

Đặt `status = CLOSED` (job sẽ biến mất khỏi các API public `/api/jobs`). **Response `200`:** `JobResponse` · **Lỗi:** `404` `Job not found`.

> Không có endpoint nào đặt `status = PAUSED` trực tiếp — muốn `PAUSED` phải dùng `PATCH .../{id}` với `"status": "PAUSED"`.

---

## 14. Admin Job API — `/api/admin/jobs`

**Quyền:** `ROLE_ADMIN`. Khác nhóm recruiter ở chỗ **thấy toàn bộ job của mọi người, kể cả `CLOSED`**.

| Method | Path | Mô tả | Request | Response |
|---|---|---|---|---|
| POST | `/api/admin/jobs` | Tạo job (`createdBy` = admin đang đăng nhập) | `JobRequest` | `201` `JobResponse` |
| GET | `/api/admin/jobs` | Tất cả job · **📄 Page** | `page`, `size`, `sort` | `200` `Page<JobResponse>` |
| GET | `/api/admin/jobs/search?title=` | Tìm theo tiêu đề, không lọc `CLOSED` · **📄 Page** | query `title` (bắt buộc) + `page`, `size`, `sort` | `200` `Page<JobResponse>` |
| PATCH | `/api/admin/jobs/{id}` | Cập nhật toàn phần | `JobRequest` | `200` `JobResponse` |
| PATCH | `/api/admin/jobs/{id}/active` | `status = OPEN` | — | `200` `JobResponse` |
| PATCH | `/api/admin/jobs/{id}/deactivate` | `status = CLOSED` | — | `200` `JobResponse` |

Chi tiết `JobRequest` và các lỗi: xem mục 13.1. Quy ước phân trang: xem mục **1.8**.

> Nhóm admin **không có** `GET /api/admin/jobs/{id}` và **không có** `DELETE` (mặc dù `JobUseCase.delete()` tồn tại, chưa endpoint nào expose). Muốn xem chi tiết một job, dùng `GET /api/jobs/{id}` (chỉ với job chưa đóng).

---

## 15. Admin Company API — `/api/admin/companies`

**Quyền:** `ROLE_ADMIN`.

### 15.1. `POST /api/admin/companies` — Tạo công ty

**Success:** `201 Created`

**Request body — `CompanyRequest`:**

| Field | Kiểu | Bắt buộc | Validation |
|---|---|:---:|---|
| `name` | string | ✅ | Không rỗng, ≤ 255 ký tự |
| `logo` | string | ❌ | ≤ 255 ký tự — tên file từ `POST /api/files/upload` |
| `email` | string | ✅ | Không rỗng, định dạng email, ≤ 255 ký tự |
| `website` | string | ❌ | ≤ 255 ký tự |
| `description` | string | ✅ | Không rỗng, **≤ 255 ký tự** |
| `address` | string | ✅ | Không rỗng, ≤ 255 ký tự |

```json
{
  "name": "FPT Software",
  "logo": "b3f1c2d4-....png",
  "email": "contact@fpt.com",
  "website": "https://fpt.com",
  "description": "Công ty phần mềm hàng đầu",
  "address": "Hà Nội"
}
```

**Response `201`:** `CompanyResponse`, `isActive` luôn = `true` khi mới tạo.

**Lỗi:** `400` validation.

> Không kiểm tra trùng tên hay email công ty — có thể tạo nhiều công ty giống nhau.

### 15.2. Các endpoint còn lại

| Method | Path | Mô tả | Request | Response |
|---|---|---|---|---|
| GET | `/api/admin/companies` | Tất cả công ty · **📄 Page** | `page`, `size`, `sort` | `200` `Page<CompanyResponse>` |
| GET | `/api/admin/companies/active` | Chỉ công ty `isActive = true` · **📄 Page** | `page`, `size`, `sort` | `200` `Page<CompanyResponse>` |
| GET | `/api/admin/companies/{id}` | Chi tiết | — | `200` `CompanyResponse`, `404` `Company not found` |
| PATCH | `/api/admin/companies/{id}` | Cập nhật **toàn phần** | `CompanyRequest` | `200` `CompanyResponse` |
| PATCH | `/api/admin/companies/{id}/active` | `isActive = true` | — | `200` `CompanyResponse` |
| PATCH | `/api/admin/companies/{id}/deactivate` | `isActive = false` | — | `200` `CompanyResponse` |

Quy ước phân trang: xem mục **1.8**.

> ⚠️ `PATCH /api/admin/companies/{id}` ghi đè toàn bộ field (kể cả set `logo`/`website` thành `null` nếu không gửi) — FE phải gửi đủ dữ liệu.
> `deactivate` công ty **không** đóng các job thuộc công ty đó; job vẫn hiển thị ở `/api/jobs`.

---

## 16. Application API — Ứng tuyển

Một ứng viên ứng tuyển được nhiều job, một job nhận được nhiều đơn (quan hệ n–n qua bảng `applications`, có dữ liệu riêng cho từng đơn).

API tách theo vai trò: ứng viên dùng `/api/applications`, recruiter dùng `/api/recruiter/applications`, admin dùng `/api/admin/applications`.

### 16.1. Luồng trạng thái (state machine)

```
APPLICATION_CREATED ──► SCREENING ──► INTERVIEW ──► OFFER ──► HIRED
        │                   │             │           │
        └───────────────────┴─────────────┴───────────┴──────► REJECTED

(mọi trạng thái chưa kết thúc) ──► WITHDRAWN   ← chỉ ứng viên được thực hiện
```

| Trạng thái hiện tại | Được chuyển sang |
|---|---|
| `APPLICATION_CREATED` | `SCREENING`, `REJECTED`, `WITHDRAWN` |
| `SCREENING` | `INTERVIEW`, `REJECTED`, `WITHDRAWN` |
| `INTERVIEW` | `OFFER`, `REJECTED`, `WITHDRAWN` |
| `OFFER` | `HIRED`, `REJECTED`, `WITHDRAWN` |
| `HIRED`, `REJECTED`, `WITHDRAWN` | *(kết thúc — không chuyển được nữa)* |

- Chuyển sai bước → **409** `Invalid application status transition from X to Y`.
- Chuyển sang **chính trạng thái hiện tại** → **200**, không lỗi (idempotent).
- **`WITHDRAWN` chỉ ứng viên đặt được** qua endpoint `withdraw`. Recruiter/admin gửi `{"status":"WITHDRAWN"}` → **409** `Only the candidate can withdraw an application`.

### 16.2. Quy ước quyền sở hữu — luôn trả `404`, không phải `403`

Truy cập đơn không thuộc về mình trả về **`404` `Application not found`** — **giống hệt** khi đơn không tồn tại. Đây là chủ ý: không xác nhận sự tồn tại của đơn người khác.

| Vai trò | Phạm vi nhìn thấy |
|---|---|
| Ứng viên | Chỉ đơn do chính mình nộp |
| Recruiter | Chỉ đơn ứng tuyển vào job **do chính mình tạo** (`job.createdBy`) |
| Admin | Tất cả |

> Job có `createdBy = null` (job mồ côi) không thuộc recruiter nào → mọi recruiter đều nhận `404`.
> `GET /api/recruiter/applications/job/{jobId}` với job của người khác trả `200 []` (mảng rỗng), không phải `404`.

### 16.3. `POST /api/applications` — Ứng tuyển vào job

**Quyền:** `ROLE_CANDIDATE` · **Success:** `201 Created`

**Request body — `JobApplicationRequest`:**

| Field | Kiểu | Bắt buộc | Validation |
|---|---|:---:|---|
| `jobId` | number | ✅ | `Job is required` |
| `resumeId` | number | ❌ | CV **phải thuộc về chính ứng viên đang đăng nhập** |
| `source` | string | ❌ | ≤ 100 ký tự |
| `expectedSalary` | number | ❌ | ≥ 0 |
| `note` | string | ❌ | — |

> ⚠️ **Không gửi `candidateId` / `status`** — hai field này không tồn tại trong request. Server tự lấy `candidateId` từ JWT và luôn đặt `status = APPLICATION_CREATED`. Gửi thừa sẽ bị bỏ qua.

**Ví dụ request:**

```json
{
  "jobId": 3,
  "resumeId": 1,
  "source": "web",
  "expectedSalary": 1500,
  "note": "Mong muốn làm việc tại HN"
}
```

**Ví dụ response `201`:**

```json
{
  "message": "create success",
  "data": {
    "id": 1,
    "candidateId": 6,
    "jobId": 3,
    "resumeId": 1,
    "status": "APPLICATION_CREATED",
    "source": "web",
    "expectedSalary": 1500,
    "note": "Mong muốn làm việc tại HN",
    "appliedAt": 1784211852.801741600,
    "updatedAt": null
  }
}
```

**Lỗi:**

| Status | Message | Khi nào |
|---|---|---|
| `400` | `Job is required` | Thiếu `jobId` |
| `403` | *(body rỗng)* | Token không phải `ROLE_CANDIDATE` |
| `404` | `Job not found` | `jobId` không tồn tại |
| `404` | `Resume not found` | CV không tồn tại **hoặc** là CV của ứng viên khác |
| `409` | `Candidate has already applied to this job` | Đã ứng tuyển job này rồi |
| `409` | `Candidate can only apply to an open job` | Job đang `PAUSED` / `CLOSED` |

> **Không apply lại được sau khi đã rút đơn** — ràng buộc trùng tính trên cặp `(candidateId, jobId)` bất kể trạng thái.

### 16.4. `GET /api/applications` — Đơn của chính mình

**Quyền:** `ROLE_CANDIDATE` · **Success:** `200` `Page<JobApplicationResponse>` · **📄 Đã phân trang** (`page`, `size`, `sort` — xem mục 1.8).

Sắp xếp mặc định: `sort=appliedAt,desc` (mới nhất trước). Mảng phần tử ở `data.content`.

### 16.5. `GET /api/applications/{id}` — Chi tiết đơn của chính mình

**Quyền:** `ROLE_CANDIDATE` · **Success:** `200` `JobApplicationResponse` · **Lỗi:** `404` `Application not found` (không tồn tại **hoặc** không phải đơn của mình)

### 16.6. `PATCH /api/applications/{id}/withdraw` — Rút đơn

**Quyền:** `ROLE_CANDIDATE` · **Success:** `200` `JobApplicationResponse` (`status = WITHDRAWN`) · **Không có request body.**

| Status | Message | Khi nào |
|---|---|---|
| `404` | `Application not found` | Không tồn tại hoặc không phải đơn của mình |
| `409` | `Invalid application status transition from HIRED to WITHDRAWN` | Đơn đã `HIRED` / `REJECTED` |

> Rút đơn đã `WITHDRAWN` → `200` (idempotent), không lỗi.

### 16.7. Recruiter — `/api/recruiter/applications`

**Quyền:** `ROLE_RECRUITER` hoặc `ROLE_ADMIN`. Mọi endpoint chỉ thấy đơn của job do chính mình tạo (mục 16.2).

| Method | Path | Mô tả | Request | Response |
|---|---|---|---|---|
| GET | `/api/recruiter/applications` | Đơn vào mọi job của mình · **📄 Page** | `page`, `size`, `sort` | `200` `Page<JobApplicationResponse>` |
| GET | `/api/recruiter/applications/job/{jobId}` | Đơn theo một job của mình · **📄 Page** | `page`, `size`, `sort` | `200` `Page<JobApplicationResponse>` (job người khác → `content = []`) |
| GET | `/api/recruiter/applications/{id}` | Chi tiết một đơn | — | `200` `JobApplicationResponse`, `404` |
| PATCH | `/api/recruiter/applications/{id}/status` | Chuyển trạng thái | `ApplicationStatusRequest` | `200` `JobApplicationResponse` |

Quy ước phân trang: mặc định `sort=appliedAt,desc` — xem mục **1.8**.

**`ApplicationStatusRequest`:**

| Field | Kiểu | Bắt buộc | Validation |
|---|---|:---:|---|
| `status` | `ApplicationStatus` | ✅ | `Status is required`. Không nhận `WITHDRAWN` |

**Ví dụ:** `PATCH /api/recruiter/applications/1/status` với `{"status":"SCREENING"}` → `200`, `status = SCREENING`, `updatedAt` được cập nhật.

**Lỗi:** `400` `Status is required` · `400` `Malformed request or unsupported enum value` · `404` `Application not found` · `409` sai bước chuyển · `409` `Only the candidate can withdraw an application`

### 16.8. Admin — `/api/admin/applications`

**Quyền:** `ROLE_ADMIN`. Không giới hạn phạm vi.

| Method | Path | Mô tả | Request | Response |
|---|---|---|---|---|
| GET | `/api/admin/applications` | Tất cả đơn · **📄 Page** | `page`, `size`, `sort` | `200` `Page<JobApplicationResponse>` |
| GET | `/api/admin/applications/{id}` | Chi tiết đơn bất kỳ | — | `200`, `404` `Application not found` |
| GET | `/api/admin/applications/job/{jobId}` | Đơn theo job bất kỳ · **📄 Page** | `page`, `size`, `sort` | `200` `Page<JobApplicationResponse>` |
| GET | `/api/admin/applications/candidate/{candidateId}` | Đơn theo ứng viên · **📄 Page** | `page`, `size`, `sort` | `200` `Page<JobApplicationResponse>` |
| PATCH | `/api/admin/applications/{id}/status` | Chuyển trạng thái | `ApplicationStatusRequest` | `200` `JobApplicationResponse` |
| DELETE | `/api/admin/applications/{id}` | Xoá đơn | — | `200` `{"message":"delete success","data":null}`, `404` |

Quy ước phân trang: mặc định `sort=appliedAt,desc` — xem mục **1.8**.

> Admin cũng **không** đặt được `WITHDRAWN` và **vẫn** phải tuân thủ state machine — không có đường vòng.

---

## 17. Admin User API — `/api/admin/users`

Xem danh sách và bật/tắt tài khoản người dùng. **Quyền: `ROLE_ADMIN`** cho mọi endpoint.

Cờ trạng thái là `User.active` kiểu **`Boolean`** (cột `users.is_active`): kích hoạt → `true`, vô hiệu hoá → `false`.

### 17.1. `GET /api/admin/users` — Danh sách tất cả người dùng

**Quyền:** `ROLE_ADMIN` · **Success:** `200` `Page<UserResponse>` · **📄 Đã phân trang** (`page`, `size`, `sort` — xem mục 1.8, mặc định `sort=id,asc`).

Trả về **mọi** user thuộc mọi role (`ADMIN`, `RECRUITER`, `CANDIDATE`), cả đang hoạt động lẫn đã bị vô hiệu hoá.

**Ví dụ response:**

```json
{
  "message": "success",
  "data": {
    "content": [
      {
        "id": 1,
        "email": "admin@example.com",
        "fullname": "Admin",
        "phone": "0900000000",
        "avatarUrl": "default-avatar.jpg",
        "active": true,
        "role": "ADMIN"
      },
      {
        "id": 6,
        "email": "a.nguyen@example.com",
        "fullname": "Nguyen Van A",
        "phone": "0900000001",
        "avatarUrl": "default-avatar.jpg",
        "active": false,
        "role": "CANDIDATE"
      }
    ],
    "totalElements": 2,
    "totalPages": 1,
    "number": 0,
    "size": 10,
    "first": true,
    "last": true,
    "empty": false
  }
}
```

**Lỗi:** `401` *(body rỗng — thiếu token)* · `403` *(body rỗng — không phải ADMIN)*

> Response **không** chứa thông tin hồ sơ Candidate/Recruiter — muốn lấy cần gọi `/api/candidates` hoặc `/api/recruiters`.

**Ghi chú hiệu năng (cho người bảo trì):** `UserEntity.candidate` và `UserEntity.recruiter` là `@OneToOne(mappedBy = ...)` nên mặc định **EAGER** và phía nghịch của `@OneToOne` không lazy-proxy được — nếu dùng `findAll()` thuần sẽ tốn **2N+1 query**. Repository dùng `@EntityGraph(attributePaths = {"candidate", "recruiter"})` trên `Page<UserEntity> findAll(Pageable)` để Hibernate tách 1 truy vấn count + 1 truy vấn data với fetch join, vừa giữ đúng `Page<T>` vừa gỡ được 2N+1.

### 17.1a. `GET /api/admin/users/{id}` — Chi tiết user theo id

**Quyền:** `ROLE_ADMIN` · **Success:** `200` `UserResponse` · **Lỗi:** `404` `User not found with id: {id}`

### 17.1b. `GET /api/admin/users/search?keyword=&role=` — Tìm user theo fullname/email + role

**Quyền:** `ROLE_ADMIN` · **Success:** `200` `Page<UserResponse>` · **📄 Đã phân trang** (`page`, `size`, `sort` — xem mục 1.8, mặc định `sort=id,asc`).

| Query param | Kiểu | Bắt buộc | Ghi chú |
|---|---|:---:|---|
| `keyword` | string | ❌ | Tìm gần đúng theo `fullname` **hoặc** `email` (LIKE, prefix). Bỏ trống → khớp mọi user thoả `role`. |
| `role` | `Role` | ❌ | Lọc theo `ADMIN` \| `RECRUITER` \| `CANDIDATE`. **Bỏ trống hoặc gửi sai enum → 400** — endpoint hiện không hỗ trợ tìm "mọi role". |
| `page`, `size`, `sort` | | ❌ | Mặc định `page=0`, `size=10`, `sort=id,asc` |

**Ví dụ:** `GET /api/admin/users/search?keyword=nguy&role=CANDIDATE&page=0&size=20`

### 17.2. Vô hiệu hoá tài khoản có tác dụng gì?

Đây **không chỉ là đổi cờ hiển thị** — `AuthService` chặn thật:

| Hành động của user đã bị `active = false` | Kết quả |
|---|---|
| `POST /api/auth/login` | **409** `User is inactive` |
| `POST /api/auth/refresh-token` | **409** `User is inactive` |
| Gọi API bằng access token **đã cấp trước đó** | ⚠️ **Vẫn chạy được cho tới khi token hết hạn (tối đa 5 phút)** |

> ⚠️ Hệ thống **không** kiểm tra `active` trên từng request (chỉ kiểm tra lúc login/refresh). Vô hiệu hoá **không** đá user ra ngay lập tức — họ vẫn dùng được access token cũ đến khi hết hạn, sau đó không refresh được nữa. Cần chặn tức thì thì phải thêm kiểm tra `active` ở tầng filter.

### 17.3. `PATCH /api/admin/users/{id}/active` — Kích hoạt tài khoản

**Quyền:** `ROLE_ADMIN` · **Success:** `200` · **Không có request body.**

Đặt `active = true` và cập nhật `updatedAt`. Gọi trên tài khoản đang active → vẫn `200` (idempotent).

**Response:**

```json
{
  "message": "success",
  "data": {
    "id": 6,
    "email": "a.nguyen@example.com",
    "fullname": "Nguyen Van A",
    "phone": "0900000001",
    "avatarUrl": "default-avatar.jpg",
    "active": true,
    "role": "CANDIDATE"
  }
}
```

**Lỗi:** `401` *(body rỗng — thiếu token)* · `403` *(body rỗng — không phải ADMIN)* · `404` `User not found with id: {id}`

### 17.4. `PATCH /api/admin/users/{id}/deactivate` — Vô hiệu hoá tài khoản

**Quyền:** `ROLE_ADMIN` · **Success:** `200` (`active = false`) · **Không có request body.**

Đặt `active = false` và cập nhật `updatedAt`. Idempotent như trên.

**Lỗi:**

| Status | Message | Khi nào |
|---|---|---|
| `401` | *(body rỗng)* | Thiếu token |
| `403` | *(body rỗng)* | Không phải `ROLE_ADMIN` |
| `404` | `User not found with id: {id}` | `id` không tồn tại |
| `409` | `Cannot deactivate your own account` | Admin tự vô hiệu hoá chính mình |

> 🔒 **Admin không tự vô hiệu hoá mình được.** Vì user inactive không đăng nhập lại được, admin tự tắt tài khoản mình sẽ **tự khoá vĩnh viễn** và chỉ sửa được bằng cách can thiệp trực tiếp vào DB. Muốn tắt tài khoản một admin thì cần **một admin khác** thực hiện.

---

## 18. Chức năng có model nhưng CHƯA có API

Các thành phần sau đã tồn tại trong code nhưng **chưa được expose thành endpoint** — FE chưa thể tích hợp:

| Thành phần | Trạng thái |
|---|---|
| `JobUseCase.delete(Long id)` | Có ở service, **chưa có** endpoint `DELETE /api/…/jobs/{id}` |
| `CompanyUseCase.findById(Long id)` | Vẫn dùng nội bộ (`RecruiterService`, admin) — bản public dùng `findActiveById` (mục 9.3) |
