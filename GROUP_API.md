# GROUP API — ATS (Applicant Tracking System)

Bảng tổng hợp toàn bộ REST API, nhóm theo controller/tính năng. Không mô tả chi tiết field/response — xem [DOCUMENT_API.md](DOCUMENT_API.md) để biết request/response schema, validation, mã lỗi.

`📄 Page` = endpoint đã phân trang server-side (`Page<T>`, query `page`/`size`/`sort`) — xem mục 1.8 của `DOCUMENT_API.md`.

---

## 1. Auth — `/api/auth`

| # | Method | Path | Quyền | Mô tả |
|---:|---|---|---|---|
| 1 | POST | `/api/auth/register` | Public | Đăng ký ứng viên |
| 2 | POST | `/api/auth/register/recruiter` | Public | Đăng ký nhà tuyển dụng |
| 3 | POST | `/api/auth/login` | Public | Đăng nhập |
| 4 | POST | `/api/auth/refresh-token` | Public | Cấp lại access token |
| 5 | POST | `/api/auth/logout` | Đăng nhập | Thu hồi refresh token |

## 2. User — `/api/users`

| # | Method | Path | Quyền | Mô tả |
|---:|---|---|---|---|
| 6 | PATCH | `/api/users/password` | Đăng nhập | Đổi mật khẩu |
| 7 | PATCH | `/api/users` | Đăng nhập | Cập nhật thông tin user |

## 3. File — `/api/files`, `/uploads`

| # | Method | Path | Quyền | Mô tả |
|---:|---|---|---|---|
| 8 | POST | `/api/files/upload` | Public | Upload file |
| 9 | GET | `/uploads/{fileName}` | Public | Tải file tĩnh |

## 4. Job công khai — `/api/jobs`

| # | Method | Path | Quyền | Mô tả |
|---:|---|---|---|---|
| 10 | GET | `/api/jobs` | Public | Danh sách job chưa đóng · 📄 Page |
| 11 | GET | `/api/jobs/search?title=` | Public | Tìm job theo tiêu đề · 📄 Page |
| 12 | GET | `/api/jobs/{id}` | Public | Chi tiết job |

## 5. Company công khai — `/api/companies`

| # | Method | Path | Quyền | Mô tả |
|---:|---|---|---|---|
| 13 | GET | `/api/companies` | Public | Danh sách công ty đang hoạt động · 📄 Page |
| 14 | GET | `/api/companies/search?name=` | Public | Tìm công ty đang hoạt động theo tên · 📄 Page |
| 15 | GET | `/api/companies/{id}` | Public | Chi tiết công ty đang hoạt động |

## 6. Candidate — `/api/candidates`

| # | Method | Path | Quyền | Mô tả |
|---:|---|---|---|---|
| 16 | POST | `/api/candidates` | Đăng nhập | Tạo ứng viên |
| 17 | GET | `/api/candidates` | Đăng nhập | Danh sách ứng viên · 📄 Page |
| 18 | GET | `/api/candidates/profile` | Đăng nhập | Hồ sơ ứng viên đang đăng nhập |
| 19 | PATCH | `/api/candidates` | Đăng nhập | Cập nhật hồ sơ ứng viên |

## 7. Resume — `/api/resumes`

| # | Method | Path | Quyền | Mô tả |
|---:|---|---|---|---|
| 20 | POST | `/api/resumes` | Đăng nhập | Tạo CV |
| 21 | GET | `/api/resumes` | Đăng nhập | Danh sách CV · 📄 Page |
| 22 | GET | `/api/resumes/{id}` | Đăng nhập | Chi tiết CV |
| 23 | GET | `/api/resumes/candidate/{candidateId}` | Đăng nhập | CV theo ứng viên · 📄 Page |
| 24 | PATCH | `/api/resumes/{id}` | Đăng nhập | Cập nhật CV |
| 25 | DELETE | `/api/resumes/{id}` | Đăng nhập | Xoá CV |

## 8. Recruiter — `/api/recruiters`

| # | Method | Path | Quyền | Mô tả |
|---:|---|---|---|---|
| 26 | GET | `/api/recruiters` | Đăng nhập | Danh sách recruiter · 📄 Page |
| 27 | GET | `/api/recruiters/profile` | Đăng nhập | Hồ sơ recruiter đang đăng nhập |
| 28 | PATCH | `/api/recruiters/{id}` | Đăng nhập | Cập nhật recruiter |

## 9. Recruiter Job — `/api/recruiter/jobs`

| # | Method | Path | Quyền | Mô tả |
|---:|---|---|---|---|
| 29 | POST | `/api/recruiter/jobs` | RECRUITER, ADMIN | Recruiter tạo job |
| 30 | GET | `/api/recruiter/jobs` | RECRUITER, ADMIN | Job do mình tạo · 📄 Page |
| 31 | GET | `/api/recruiter/jobs/search?title=` | RECRUITER, ADMIN | Tìm job của mình · 📄 Page |
| 32 | PATCH | `/api/recruiter/jobs/{id}` | RECRUITER, ADMIN | Cập nhật job |
| 33 | PATCH | `/api/recruiter/jobs/{id}/active` | RECRUITER, ADMIN | Mở job (`OPEN`) |
| 34 | PATCH | `/api/recruiter/jobs/{id}/deactivate` | RECRUITER, ADMIN | Đóng job (`CLOSED`) |

## 10. Admin Job — `/api/admin/jobs`

| # | Method | Path | Quyền | Mô tả |
|---:|---|---|---|---|
| 35 | POST | `/api/admin/jobs` | ADMIN | Admin tạo job |
| 36 | GET | `/api/admin/jobs` | ADMIN | Tất cả job (kể cả `CLOSED`) · 📄 Page |
| 37 | GET | `/api/admin/jobs/search?title=` | ADMIN | Tìm job (kể cả `CLOSED`) · 📄 Page |
| 38 | PATCH | `/api/admin/jobs/{id}` | ADMIN | Cập nhật job |
| 39 | PATCH | `/api/admin/jobs/{id}/active` | ADMIN | Mở job |
| 40 | PATCH | `/api/admin/jobs/{id}/deactivate` | ADMIN | Đóng job |

## 11. Admin Company — `/api/admin/companies`

| # | Method | Path | Quyền | Mô tả |
|---:|---|---|---|---|
| 41 | POST | `/api/admin/companies` | ADMIN | Tạo công ty |
| 42 | GET | `/api/admin/companies` | ADMIN | Tất cả công ty · 📄 Page |
| 43 | GET | `/api/admin/companies/active` | ADMIN | Công ty đang hoạt động · 📄 Page |
| 44 | GET | `/api/admin/companies/{id}` | ADMIN | Chi tiết công ty (kể cả đã ngưng) |
| 45 | PATCH | `/api/admin/companies/{id}` | ADMIN | Cập nhật công ty |
| 46 | PATCH | `/api/admin/companies/{id}/active` | ADMIN | Kích hoạt công ty |
| 47 | PATCH | `/api/admin/companies/{id}/deactivate` | ADMIN | Ngưng hoạt động công ty |

## 12. Application — Candidate — `/api/applications`

| # | Method | Path | Quyền | Mô tả |
|---:|---|---|---|---|
| 48 | POST | `/api/applications` | CANDIDATE | Ứng tuyển vào job |
| 49 | GET | `/api/applications` | CANDIDATE | Đơn ứng tuyển của chính mình · 📄 Page |
| 50 | GET | `/api/applications/{id}` | CANDIDATE | Chi tiết đơn của chính mình |
| 51 | PATCH | `/api/applications/{id}/withdraw` | CANDIDATE | Rút đơn ứng tuyển |

## 13. Application — Recruiter — `/api/recruiter/applications`

| # | Method | Path | Quyền | Mô tả |
|---:|---|---|---|---|
| 52 | GET | `/api/recruiter/applications` | RECRUITER, ADMIN | Đơn ứng tuyển vào job do mình tạo · 📄 Page |
| 53 | GET | `/api/recruiter/applications/job/{jobId}` | RECRUITER, ADMIN | Đơn theo job (chỉ job của mình) · 📄 Page |
| 54 | GET | `/api/recruiter/applications/{id}` | RECRUITER, ADMIN | Chi tiết đơn (chỉ job của mình) |
| 55 | PATCH | `/api/recruiter/applications/{id}/status` | RECRUITER, ADMIN | Chuyển trạng thái đơn |

## 14. Application — Admin — `/api/admin/applications`

| # | Method | Path | Quyền | Mô tả |
|---:|---|---|---|---|
| 56 | GET | `/api/admin/applications` | ADMIN | Tất cả đơn ứng tuyển · 📄 Page |
| 57 | GET | `/api/admin/applications/{id}` | ADMIN | Chi tiết đơn bất kỳ |
| 58 | GET | `/api/admin/applications/job/{jobId}` | ADMIN | Đơn theo job bất kỳ · 📄 Page |
| 59 | GET | `/api/admin/applications/candidate/{candidateId}` | ADMIN | Đơn theo ứng viên · 📄 Page |
| 60 | PATCH | `/api/admin/applications/{id}/status` | ADMIN | Chuyển trạng thái đơn |
| 61 | DELETE | `/api/admin/applications/{id}` | ADMIN | Xoá đơn ứng tuyển |

## 15. Admin User — `/api/admin/users`

| # | Method | Path | Quyền | Mô tả |
|---:|---|---|---|---|
| 62 | GET | `/api/admin/users` | ADMIN | Danh sách tất cả người dùng · 📄 Page |
| 63 | PATCH | `/api/admin/users/{id}/active` | ADMIN | Kích hoạt tài khoản (`active = true`) |
| 64 | PATCH | `/api/admin/users/{id}/deactivate` | ADMIN | Vô hiệu hoá tài khoản (`active = false`) |
| 65 | GET | `/api/admin/users/{id}` | ADMIN | Chi tiết user theo id |
| 66 | GET | `/api/admin/users/search?keyword=&role=` | ADMIN | Tìm user theo fullname/email + role · 📄 Page |
