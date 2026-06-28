# Group API

Tai lieu nay gom nhom cac API hien co trong du an ATS theo controller. Tat ca response chuan cua cac API nghiep vu dang co dang:

```json
{
  "message": "success",
  "data": {}
}
```

Ghi chu: theo `SecurityConfig` hien tai, tat ca request dang duoc `permitAll()`, nen cac API chua bi chan boi Spring Security.

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
| `GET` | `/api/candidates/profile` | Code hien tai nhan `Long id` nhung chua gan `@RequestParam`/`@PathVariable` | Lay ho so cua mot ung vien theo `id`. Theo code hien tai, endpoint nay co the can bo sung cach lay `id` ro rang. | `CandidateResponse` |
| `PATCH` | `/api/candidates` | `fullName`, `email`, `phone`, `password`, `avatar`, `linkedinUrl`, `githubUrl`, `portfolioUrl`, `currentPosition`, `yearsOfExperience` | Cap nhat ho so ung vien. Code hien tai dang tam gan `id = 1L`, nen se cap nhat candidate co id bang 1. | `CandidateResponse` |

## 3. Recruiter API

Base path: `/api/recruiters`

| Method | Endpoint | Body / Param chinh | Tac dung | Response chinh |
|---|---|---|---|---|
| `GET` | `/api/recruiters` | Khong co | Lay danh sach tat ca nha tuyen dung. | `List<RecruiterResponse>` |
| `GET` | `/api/recruiters/{id}` | Path variable `id` | Lay chi tiet mot nha tuyen dung theo id. | `RecruiterResponse` |
| `PATCH` | `/api/recruiters/{id}` | Path variable `id`; body gom `fullName`, `email`, `phone`, `password`, `avatar`, `companyId`, `position` | Cap nhat thong tin nha tuyen dung va thong tin user lien quan. | `RecruiterResponse` |

## 4. Company API

Base path: `/api/companies`

| Method | Endpoint | Body / Param chinh | Tac dung | Response chinh |
|---|---|---|---|---|
| `POST` | `/api/companies` | `name`, `logo`, `email`, `website`, `description`, `address` | Tao cong ty moi trong he thong. | `CompanyResponse` |
| `GET` | `/api/companies` | Khong co | Lay danh sach tat ca cong ty. | `List<CompanyResponse>` |
| `GET` | `/api/companies/{id}` | Path variable `id` | Lay chi tiet cong ty theo id. | `CompanyResponse` |
| `PATCH` | `/api/companies/{id}` | Path variable `id`; body gom `name`, `logo`, `email`, `website`, `description`, `address` | Cap nhat thong tin cong ty. | `CompanyResponse` |

## 5. User API

Base path: `/api/users`

| Method | Endpoint | Body / Param chinh | Tac dung | Response chinh |
|---|---|---|---|---|
| `PATCH` | `/api/users/password` | `oldPassword`, `newPassword`, `confirmPassword` | Doi mat khau user. Code hien tai co tham so `Long id` nhung chua gan annotation va dang tam gan `id = 1L`, nen API se doi mat khau cho user id 1. | `data = null` |

## 6. File API

Base path: `/api/files`

| Method | Endpoint | Body / Param chinh | Tac dung | Response chinh |
|---|---|---|---|---|
| `POST` | `/api/files/upload` | `multipart/form-data` voi field `file` | Upload file len storage cua server, phu hop de upload avatar, CV/resume hoac tai lieu lien quan. | `UploadResponse` gom `originalName`, `storedName`, `url`, `size` |

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
| `POST` | `/api/jobs` | `title`, `description`, `requirements`, `location`, `employmentType`, `salaryMin`, `salaryMax`, `status`, `createdBy` | Tao job moi. | `JobResponse` |
| `GET` | `/api/jobs` | Khong co | Lay danh sach tat ca job. | `List<JobResponse>` |
| `GET` | `/api/jobs/{id}` | Path variable `id` | Lay chi tiet job theo id. | `JobResponse` |
| `PATCH` | `/api/jobs/{id}` | Path variable `id`; body nhu tao job | Cap nhat job. | `JobResponse` |
| `DELETE` | `/api/jobs/{id}` | Path variable `id` | Xoa job. | `data = null` |

## 9. Job Application API

Base path: `/api/applications`

| Method | Endpoint | Body / Param chinh | Tac dung | Response chinh |
|---|---|---|---|---|
| `POST` | `/api/applications` | `candidateId`, `jobId`, `stageId`, `status`, `source`, `expectedSalary`, `note` | Tao don ung tuyen moi. | `JobApplicationResponse` |
| `GET` | `/api/applications` | Query optional `candidateId`, `jobId` | Lay danh sach application; neu co query thi loc theo candidate hoac job. | `List<JobApplicationResponse>` |
| `GET` | `/api/applications/{id}` | Path variable `id` | Lay chi tiet application theo id. | `JobApplicationResponse` |
| `GET` | `/api/applications/candidate/{candidateId}` | Path variable `candidateId` | Lay application theo candidate. | `List<JobApplicationResponse>` |
| `GET` | `/api/applications/job/{jobId}` | Path variable `jobId` | Lay application theo job. | `List<JobApplicationResponse>` |
| `PATCH` | `/api/applications/{id}` | Path variable `id`; body nhu tao application | Cap nhat application. | `JobApplicationResponse` |
| `PATCH` | `/api/applications/{id}/status` | Path variable `id`; body `status`, `stageId` | Doi trang thai va stage cua application. | `JobApplicationResponse` |
| `DELETE` | `/api/applications/{id}` | Path variable `id` | Xoa application. | `data = null` |

## 10. Application Stage API

Base path: `/api/application-stages`

| Method | Endpoint | Body / Param chinh | Tac dung | Response chinh |
|---|---|---|---|---|
| `POST` | `/api/application-stages` | `name`, `position` | Tao stage moi cho pipeline ung tuyen. | `ApplicationStageResponse` |
| `GET` | `/api/application-stages` | Khong co | Lay danh sach stage. | `List<ApplicationStageResponse>` |
| `GET` | `/api/application-stages/{id}` | Path variable `id` | Lay stage theo id. | `ApplicationStageResponse` |
| `PATCH` | `/api/application-stages/{id}` | Path variable `id`; body `name`, `position` | Cap nhat stage. | `ApplicationStageResponse` |
| `DELETE` | `/api/application-stages/{id}` | Path variable `id` | Xoa stage. | `data = null` |

## 11. Interview API

Base path: `/api/interviews`

| Method | Endpoint | Body / Param chinh | Tac dung | Response chinh |
|---|---|---|---|---|
| `POST` | `/api/interviews` | `jobApplicationId`, `interviewerId`, `title`, `interviewTime`, `location`, `status` | Tao lich phong van. | `InterviewResponse` |
| `GET` | `/api/interviews` | Khong co | Lay danh sach interview. | `List<InterviewResponse>` |
| `GET` | `/api/interviews/{id}` | Path variable `id` | Lay interview theo id. | `InterviewResponse` |
| `GET` | `/api/interviews/application/{applicationId}` | Path variable `applicationId` | Lay interview theo application. | `List<InterviewResponse>` |
| `PATCH` | `/api/interviews/{id}` | Path variable `id`; body nhu tao interview | Cap nhat interview. | `InterviewResponse` |
| `PATCH` | `/api/interviews/{id}/status` | Path variable `id`; body `status` | Doi trang thai interview. | `InterviewResponse` |
| `DELETE` | `/api/interviews/{id}` | Path variable `id` | Xoa interview. | `data = null` |

## 12. Interview Feedback API

Base path: `/api/interview-feedbacks`

| Method | Endpoint | Body / Param chinh | Tac dung | Response chinh |
|---|---|---|---|---|
| `POST` | `/api/interview-feedbacks` | `interviewId`, `recruiterId`, `rating`, `comment`, `recommendation` | Tao feedback cho buoi phong van. | `InterviewFeedbackResponse` |
| `GET` | `/api/interview-feedbacks` | Khong co | Lay danh sach feedback. | `List<InterviewFeedbackResponse>` |
| `GET` | `/api/interview-feedbacks/{id}` | Path variable `id` | Lay feedback theo id. | `InterviewFeedbackResponse` |
| `PATCH` | `/api/interview-feedbacks/{id}` | Path variable `id`; body nhu tao feedback | Cap nhat feedback. | `InterviewFeedbackResponse` |
| `DELETE` | `/api/interview-feedbacks/{id}` | Path variable `id` | Xoa feedback. | `data = null` |

## 13. Activity Log API

Base path: `/api/activity-logs`

| Method | Endpoint | Body / Param chinh | Tac dung | Response chinh |
|---|---|---|---|---|
| `POST` | `/api/activity-logs` | `userId`, `action`, `entityType`, `entityId`, `description` | Tao activity log. | `ActivityLogResponse` |
| `GET` | `/api/activity-logs` | Khong co | Lay danh sach activity log. | `List<ActivityLogResponse>` |
| `GET` | `/api/activity-logs/{id}` | Path variable `id` | Lay activity log theo id. | `ActivityLogResponse` |
| `GET` | `/api/activity-logs/user/{userId}` | Path variable `userId` | Lay activity log theo user. | `List<ActivityLogResponse>` |
| `DELETE` | `/api/activity-logs/{id}` | Path variable `id` | Xoa activity log. | `data = null` |
