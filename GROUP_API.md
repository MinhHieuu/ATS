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
| 97 | GET | `/api/jobs/category/{categoryId}` | Public | Job chưa đóng theo ngành nghề · 📄 Page |

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
| 108 | GET | `/api/recruiter/jobs/category/{categoryId}` | RECRUITER, ADMIN | Job của mình theo ngành nghề · 📄 Page |
| 32 | PATCH | `/api/recruiter/jobs/{id}` | RECRUITER, ADMIN | Cập nhật job |
| 33 | PATCH | `/api/recruiter/jobs/{id}/active` | RECRUITER, ADMIN | Mở job (`OPEN`) |
| 34 | PATCH | `/api/recruiter/jobs/{id}/deactivate` | RECRUITER, ADMIN | Đóng job (`CLOSED`) |

## 10. Admin Job — `/api/admin/jobs`

| # | Method | Path | Quyền | Mô tả |
|---:|---|---|---|---|
| 35 | POST | `/api/admin/jobs` | ADMIN | Admin tạo job |
| 36 | GET | `/api/admin/jobs` | ADMIN | Tất cả job (kể cả `CLOSED`) · 📄 Page |
| 37 | GET | `/api/admin/jobs/search?title=` | ADMIN | Tìm job (kể cả `CLOSED`) · 📄 Page |
| 109 | GET | `/api/admin/jobs/category/{categoryId}` | ADMIN | Job theo ngành nghề (kể cả `CLOSED`) · 📄 Page |
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

## 16. Notification — `/api/notifications`

| # | Method | Path | Quyền | Mô tả |
|---:|---|---|---|---|
| 67 | GET | `/api/notifications` | Đăng nhập | Thông báo của tôi · 📄 Page |
| 68 | GET | `/api/notifications/unread-count` | Đăng nhập | Số thông báo chưa đọc |
| 69 | PATCH | `/api/notifications/{id}/read` | Đăng nhập | Đánh dấu đã đọc |
| 70 | PATCH | `/api/notifications/read-all` | Đăng nhập | Đánh dấu tất cả đã đọc |

## 17. Interview — Candidate — `/api/interviews`

| # | Method | Path | Quyền | Mô tả |
|---:|---|---|---|---|
| 71 | GET | `/api/interviews` | CANDIDATE | Lịch phỏng vấn của tôi · 📄 Page |
| 72 | GET | `/api/interviews/{id}` | CANDIDATE | Chi tiết lịch phỏng vấn |

## 18. Interview — Recruiter — `/api/recruiter/interviews`

| # | Method | Path | Quyền | Mô tả |
|---:|---|---|---|---|
| 73 | POST | `/api/recruiter/interviews` | RECRUITER, ADMIN | Tạo lịch phỏng vấn |
| 74 | GET | `/api/recruiter/interviews` | RECRUITER, ADMIN | Lịch phỏng vấn (job của mình) · 📄 Page |
| 75 | GET | `/api/recruiter/interviews/{id}` | RECRUITER, ADMIN | Chi tiết lịch phỏng vấn |
| 76 | GET | `/api/recruiter/interviews/application/{applicationId}` | RECRUITER, ADMIN | Lịch theo đơn ứng tuyển · 📄 Page |
| 77 | PATCH | `/api/recruiter/interviews/{id}` | RECRUITER, ADMIN | Cập nhật lịch phỏng vấn |
| 78 | PATCH | `/api/recruiter/interviews/{id}/result` | RECRUITER, ADMIN | Cập nhật kết quả phỏng vấn |
| 79 | DELETE | `/api/recruiter/interviews/{id}` | RECRUITER, ADMIN | Xoá lịch phỏng vấn |

## 19. Interview — Admin — `/api/admin/interviews`

| # | Method | Path | Quyền | Mô tả |
|---:|---|---|---|---|
| 80 | POST | `/api/admin/interviews` | ADMIN | Tạo lịch phỏng vấn |
| 81 | GET | `/api/admin/interviews` | ADMIN | Tất cả lịch phỏng vấn · 📄 Page |
| 82 | GET | `/api/admin/interviews/{id}` | ADMIN | Chi tiết lịch phỏng vấn |
| 83 | GET | `/api/admin/interviews/application/{applicationId}` | ADMIN | Lịch theo đơn ứng tuyển · 📄 Page |
| 84 | PATCH | `/api/admin/interviews/{id}` | ADMIN | Cập nhật lịch phỏng vấn |
| 85 | PATCH | `/api/admin/interviews/{id}/result` | ADMIN | Cập nhật kết quả phỏng vấn |
| 86 | DELETE | `/api/admin/interviews/{id}` | ADMIN | Xoá lịch phỏng vấn |

## 20. Interview Feedback — Recruiter — `/api/recruiter/interview-feedbacks`

| # | Method | Path | Quyền | Mô tả |
|---:|---|---|---|---|
| 87 | POST | `/api/recruiter/interview-feedbacks` | RECRUITER, ADMIN | Tạo đánh giá phỏng vấn |
| 88 | GET | `/api/recruiter/interview-feedbacks` | RECRUITER, ADMIN | Đánh giá (job của mình) · 📄 Page |
| 89 | GET | `/api/recruiter/interview-feedbacks/{id}` | RECRUITER, ADMIN | Chi tiết đánh giá |
| 90 | GET | `/api/recruiter/interview-feedbacks/interview/{interviewId}` | RECRUITER, ADMIN | Đánh giá theo buổi phỏng vấn · 📄 Page |
| 91 | PATCH | `/api/recruiter/interview-feedbacks/{id}` | RECRUITER, ADMIN | Cập nhật đánh giá (chỉ người viết) |
| 92 | DELETE | `/api/recruiter/interview-feedbacks/{id}` | RECRUITER, ADMIN | Xoá đánh giá (chỉ người viết) |

## 21. Interview Feedback — Admin — `/api/admin/interview-feedbacks`

| # | Method | Path | Quyền | Mô tả |
|---:|---|---|---|---|
| 93 | GET | `/api/admin/interview-feedbacks` | ADMIN | Tất cả đánh giá · 📄 Page |
| 94 | GET | `/api/admin/interview-feedbacks/{id}` | ADMIN | Chi tiết đánh giá |
| 95 | GET | `/api/admin/interview-feedbacks/interview/{interviewId}` | ADMIN | Đánh giá theo buổi phỏng vấn · 📄 Page |
| 96 | DELETE | `/api/admin/interview-feedbacks/{id}` | ADMIN | Xoá đánh giá |

> Ứng viên **không** có endpoint nào — đánh giá phỏng vấn là dữ liệu nội bộ.

## 22. Category công khai — `/api/categories`

| # | Method | Path | Quyền | Mô tả |
|---:|---|---|---|---|
| 98 | GET | `/api/categories` | Public | Danh sách ngành nghề đang dùng · 📄 Page |
| 99 | GET | `/api/categories/search?name=` | Public | Tìm ngành nghề theo tên · 📄 Page |
| 100 | GET | `/api/categories/{id}` | Public | Chi tiết ngành nghề đang dùng |

## 23. Category — Admin — `/api/admin/categories`

| # | Method | Path | Quyền | Mô tả |
|---:|---|---|---|---|
| 101 | POST | `/api/admin/categories` | ADMIN | Tạo ngành nghề |
| 102 | GET | `/api/admin/categories` | ADMIN | Tất cả ngành nghề · 📄 Page |
| 103 | GET | `/api/admin/categories/active` | ADMIN | Ngành nghề đang dùng · 📄 Page |
| 104 | GET | `/api/admin/categories/{id}` | ADMIN | Chi tiết ngành nghề |
| 105 | PATCH | `/api/admin/categories/{id}` | ADMIN | Cập nhật tên/mô tả |
| 106 | PATCH | `/api/admin/categories/{id}/deactivate` | ADMIN | Ngừng dùng |
| 107 | PATCH | `/api/admin/categories/{id}/active` | ADMIN | Dùng lại |

> Không có endpoint xoá — category chỉ bật/tắt qua `isActive` để không làm mồ côi job đang trỏ tới.
> Ứng viên và recruiter dùng chung nhóm public (mục 22) nên **chỉ thấy category `isActive = true`**; recruiter cũng chỉ chọn được category đang mở khi tạo/sửa job (`409` `Category is no longer available` nếu category đã tắt). Admin thấy và dùng được toàn bộ.

## 24. Audit Log — Admin — `/api/admin/audit-logs`

| # | Method | Path | Quyền | Mô tả |
|---:|---|---|---|---|
| 110 | GET | `/api/admin/audit-logs` | ADMIN | Nhật ký hệ thống, lọc theo actor/entity/action/thời gian · 📄 Page |
| 111 | GET | `/api/admin/audit-logs/{id}` | ADMIN | Chi tiết một dòng log |
| 112 | GET | `/api/admin/audit-logs/entity/{entityType}/{entityId}` | ADMIN | Lịch sử thao tác của một đối tượng · 📄 Page |

> Chỉ ghi (không sửa/xoá được qua API). Log được ghi tự động khi có thao tác quản lý user, hồ sơ ứng viên/đơn ứng tuyển, và tin tuyển dụng. Chỉ ADMIN đọc được.

## 25. WebSocket — `/ws`

| Endpoint | Giao thức | Mô tả |
|---|---|---|
| `/ws` | STOMP over WebSocket + SockJS | Kết nối realtime, gửi token qua STOMP header `Authorization: Bearer <token>` |
| `/user/queue/notifications` | STOMP subscribe | Nhận thông báo realtime cho user đang đăng nhập |
