# Group API

Tai lieu nay gom nhom cac API hien co trong du an ATS theo controller. Tat ca response chuan cua cac API nghiep vu dang co dang:

```json
{
  "message": "success",
  "data": {}
}
```

Ghi chu: theo `SecurityConfig` hien tai, cac API public gom `OPTIONS /**`, `/api/auth/register`, `/api/auth/register/recruiter`, `/api/auth/login`, `/api/auth/refresh-token`, `/uploads/**`, `/api/companies/active`, `/ws/**`. Cac API con lai can header `Authorization: Bearer <accessToken>`.

## 1. Auth API

Base path: `/api/auth`

| Method | Endpoint | Body chinh | Tac dung | Response chinh |
|---|---|---|---|---|
| `POST` | `/api/auth/register` | `fullName`, `email`, `phone`, `password`, `avatar`, `linkedinUrl`, `githubUrl`, `portfolioUrl`, `currentPosition`, `yearsOfExperience` | Dang ky tai khoan ung vien moi va tao ho so candidate tu thong tin gui len. | `CandidateResponse` |
| `POST` | `/api/auth/register/recruiter` | `fullName`, `email`, `phone`, `password`, `avatar`, `companyId`, `position` | Dang ky tai khoan nha tuyen dung moi va gan recruiter voi cong ty neu co `companyId`. | `RecruiterResponse` |
| `POST` | `/api/auth/login` | `email`, `password` | Dang nhap bang email/password, tra ve thong tin user kem access token va refresh token. | `LoginResponse` gom `user`, `accessToken`, `refreshToken` |
| `POST` | `/api/auth/refresh-token` | `refreshToken` | Tao access token moi tu refresh token con hop le. | `AccessTokenResponse` gom `accessToken` |
| `POST` | `/api/auth/logout` | `refreshToken` | Dang xuat bang cach vo hieu hoa/xoa refresh token. | `data = null` |

## 2. Candidate API

Base path: `/api/candidates`

| Method | Endpoint | Body / Param chinh | Tac dung | Response chinh |
|---|---|---|---|---|
| `POST` | `/api/candidates` | `fullName`, `email`, `phone`, `password`, `avatar`, `linkedinUrl`, `githubUrl`, `portfolioUrl`, `currentPosition`, `yearsOfExperience` | Tao ho so ung vien moi. | `CandidateResponse` |
| `GET` | `/api/candidates` | Khong co | Lay danh sach tat ca ung vien trong he thong. | `List<CandidateResponse>` |
| `GET` | `/api/candidates/profile` | Khong co (lay `userId` tu `Authentication`) | Lay ho so candidate cua user dang dang nhap. | `CandidateResponse` |
| `PATCH` | `/api/candidates` | `fullName`, `email`, `phone`, `password`, `avatar`, `linkedinUrl`, `githubUrl`, `portfolioUrl`, `currentPosition`, `yearsOfExperience` | Cap nhat ho so candidate cua user dang dang nhap (lay `userId` tu `Authentication`); `password` trong body khong duoc ap dung. | `CandidateResponse` |

## 3. Recruiter API

Base path: `/api/recruiters`

| Method | Endpoint | Body / Param chinh | Tac dung | Response chinh |
|---|---|---|---|---|
| `GET` | `/api/recruiters` | Khong co | Lay danh sach tat ca nha tuyen dung. | `List<RecruiterResponse>` |
| `GET` | `/api/recruiters/profile` | Khong co (lay `userId` tu `Authentication`) | Lay ho so recruiter cua user dang dang nhap. | `RecruiterResponse` |
| `PATCH` | `/api/recruiters/{id}` | Path variable `id`; body gom `fullName`, `email`, `phone`, `password`, `avatar`, `companyId`, `position` | Cap nhat thong tin nha tuyen dung va thong tin user lien quan. | `RecruiterResponse` |

## 4. Company API

Base path: `/api/companies`

| Method | Endpoint | Body / Param chinh | Tac dung | Response chinh |
|---|---|---|---|---|
| `POST` | `/api/companies` | `name`, `logo`, `email`, `website`, `description`, `address` | Tao cong ty moi trong he thong. | `CompanyResponse` |
| `GET` | `/api/companies` | Khong co | Lay danh sach tat ca cong ty. | `List<CompanyResponse>` |
| `GET` | `/api/companies/active` | Khong co | Lay danh sach cong ty dang active (`isActive = true`). | `List<CompanyResponse>` |
| `GET` | `/api/companies/{id}` | Path variable `id` | Lay chi tiet cong ty theo id. | `CompanyResponse` |
| `PATCH` | `/api/companies/{id}` | Path variable `id`; body gom `name`, `logo`, `email`, `website`, `description`, `address` | Cap nhat thong tin cong ty. | `CompanyResponse` |
| `PATCH` | `/api/companies/{id}/deactivate` | Path variable `id` | Vo hieu hoa cong ty bang cach doi `isActive = false`. | `CompanyResponse` |
| `PATCH` | `/api/companies/{id}/active` | Path variable `id` | Kich hoat lai cong ty bang cach doi `isActive = true`. | `CompanyResponse` |

## 5. User API

Base path: `/api/users`

| Method | Endpoint | Body / Param chinh | Tac dung | Response chinh |
|---|---|---|---|---|
| `PATCH` | `/api/users/password` | `oldPassword`, `newPassword`, `confirmPassword` | Doi mat khau user dang dang nhap, lay user tu access token. | `data = null` |
| `PATCH` | `/api/users/{id}/password` | Path variable `id`; body `oldPassword`, `newPassword`, `confirmPassword` | Doi mat khau cho user theo id chi dinh. | `data = null` |
| `PATCH` | `/api/users` | `fullname`, `email`, `phone`, `password`, `avatar` | Cap nhat thong tin user theo `email` trong body; `password` khong duoc cap nhat tai day. | `UserResponse` |

## 6. File API

Base path: `/api/files`

| Method | Endpoint | Body / Param chinh | Tac dung | Response chinh |
|---|---|---|---|---|
| `POST` | `/api/files/upload` | `multipart/form-data` voi field `file` | Upload file len storage cua server, phu hop de upload avatar, CV/resume hoac tai lieu lien quan. | `ApiResponse<String>` voi `data` la ten file luu DB; file empty tra `data: ""` |

## 7. Resume API

Base path: `/api/resumes`

| Method | Endpoint | Body / Param chinh | Tac dung | Response chinh |
|---|---|---|---|---|
| `POST` | `/api/resumes` | `candidateId`, `fileName`, `fileUrl` | Tao resume moi cho candidate. | `ResumeResponse` |
| `GET` | `/api/resumes` | Khong co | Lay danh sach tat ca resume. | `List<ResumeResponse>` |
| `GET` | `/api/resumes/{id}` | Path variable `id` | Lay chi tiet resume theo id. | `ResumeResponse` |
| `GET` | `/api/resumes/candidate/{candidateId}` | Path variable `candidateId` | Lay danh sach resume cua mot candidate. | `List<ResumeResponse>` |
| `PATCH` | `/api/resumes/{id}` | Path variable `id`; body gom `candidateId`, `fileName`, `fileUrl` | Cap nhat resume. | `ResumeResponse` |
| `DELETE` | `/api/resumes/{id}` | Path variable `id` | Xoa resume. | `data = null` |

## 8. Job API

Base path: `/api/jobs`

| Method | Endpoint | Body / Param chinh | Tac dung | Response chinh |
|---|---|---|---|---|
| `POST` | `/api/jobs` | `title`, `description`, `requirements`, `location`, `employmentType` (enum `FULLTIME`/`PARTTIME`), `companyId`, `salaryMin`, `salaryMax`, `status`, `createdBy` | Tao job moi; `companyId` bat buoc va phai la company co that. | `JobResponse` (co nested `company`) |
| `GET` | `/api/jobs` | Khong co | Lay danh sach tat ca job. | `List<JobResponse>` |
| `GET` | `/api/jobs/{id}` | Path variable `id` | Lay chi tiet job theo id. | `JobResponse` |
| `PATCH` | `/api/jobs/{id}` | Path variable `id`; body nhu tao job | Cap nhat job. | `JobResponse` |
| `PATCH` | `/api/jobs/{id}/deactivate` | Path variable `id` | Vo hieu hoa job bang cach doi `status = CLOSED`. | `JobResponse` |
| `PATCH` | `/api/jobs/{id}/active` | Path variable `id` | Kich hoat lai job bang cach doi `status = OPEN`. | `JobResponse` |
| `DELETE` | `/api/jobs/{id}` | Path variable `id` | Xoa job. | `data = null` |

