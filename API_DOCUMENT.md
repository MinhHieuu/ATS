# API Document

Tai lieu nay mo ta request va response tra ve cho tung API hien co trong project ATS.

- Base URL mac dinh: `http://localhost:8080`
- Content-Type mac dinh: `application/json`
- Rieng API upload file dung: `multipart/form-data`
- Theo `SecurityConfig` hien tai, cac API public gom `OPTIONS /**`, `/api/auth/register`, `/api/auth/register/recruiter`, `/api/auth/login`, `/api/auth/refresh-token`, `/uploads/**`. Cac API con lai can access token hop le.

## Response format chung

Phan lon API thanh cong tra ve theo format:

```json
{
  "message": "success",
  "data": {}
}
```

Response loi tra ve theo format:

```json
{
  "timestamp": "2026-06-27T10:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Request validation failed",
  "details": {
    "email": "Email must be valid"
  }
}
```

Status loi co the gap:

| Status | Khi nao xay ra |
|---|---|
| `400 Bad Request` | Request sai validation, body sai format, enum khong hop le, tham so khong hop le |
| `404 Not Found` | Khong tim thay resource |
| `409 Conflict` | Vi pham rule nghiep vu |
| `500 Internal Server Error` | Loi luu file |

## Auth API

Base path: `/api/auth`

### 1. Dang ky candidate

`POST /api/auth/register`

Tao tai khoan user va ho so ung vien.

Request body:

```json
{
  "fullName": "Nguyen Van A",
  "email": "candidate@example.com",
  "phone": "0901234567",
  "password": "password123",
  "avatar": "https://example.com/avatar.png",
  "linkedinUrl": "https://linkedin.com/in/candidate",
  "githubUrl": "https://github.com/candidate",
  "portfolioUrl": "https://candidate.dev",
  "currentPosition": "Java Developer",
  "yearsOfExperience": 2
}
```

Validation chinh:

| Field | Rule |
|---|---|
| `fullName` | Bat buoc, toi da 100 ky tu |
| `email` | Bat buoc, dung dinh dang email, toi da 150 ky tu |
| `phone` | Bat buoc, toi da 20 ky tu |
| `password` | Toi thieu 8 ky tu neu co gui |
| `linkedinUrl`, `githubUrl`, `portfolioUrl` | Toi da 255 ky tu |
| `currentPosition` | Toi da 100 ky tu |
| `yearsOfExperience` | Lon hon hoac bang 0 |

Response thanh cong: `201 Created`

```json
{
  "message": "create success",
  "data": {
    "id": 1,
    "user": {
      "id": 1,
      "email": "candidate@example.com",
      "fullname": "Nguyen Van A",
      "phone": "0901234567",
      "avatarUrl": "https://example.com/avatar.png"
    },
    "linkedinUrl": "https://linkedin.com/in/candidate",
    "githubUrl": "https://github.com/candidate",
    "portfolioUrl": "https://candidate.dev",
    "currentPosition": "Java Developer",
    "yearsOfExperience": 2,
    "createdAt": "2026-06-27T10:00:00Z",
    "updatedAt": "2026-06-27T10:00:00Z"
  }
}
```

### 2. Dang ky recruiter

`POST /api/auth/register/recruiter`

Tao tai khoan user va ho so nha tuyen dung.

Request body:

```json
{
  "fullName": "Tran Thi B",
  "email": "recruiter@example.com",
  "phone": "0912345678",
  "password": "password123",
  "avatar": "https://example.com/recruiter.png",
  "companyId": 1,
  "position": "HR Manager"
}
```

Validation chinh:

| Field | Rule |
|---|---|
| `fullName` | Bat buoc, toi da 100 ky tu |
| `email` | Bat buoc, dung dinh dang email, toi da 150 ky tu |
| `phone` | Bat buoc, toi da 20 ky tu |
| `password` | Toi thieu 8 ky tu neu co gui |
| `position` | Toi da 255 ky tu |

Response thanh cong: `201 Created`

```json
{
  "message": "create success",
  "data": {
    "id": 1,
    "user": {
      "id": 2,
      "email": "recruiter@example.com",
      "fullname": "Tran Thi B",
      "phone": "0912345678",
      "avatarUrl": "https://example.com/recruiter.png"
    },
    "company": {
      "id": 1,
      "name": "ABC Company",
      "logo": "https://example.com/logo.png",
      "email": "contact@abc.com",
      "website": "https://abc.com",
      "description": "Software company",
      "address": "Ho Chi Minh City",
      "createdAt": "2026-06-27T10:00:00Z",
      "updatedAt": "2026-06-27T10:00:00Z"
    },
    "position": "HR Manager"
  }
}
```

### 3. Dang nhap

`POST /api/auth/login`

Dang nhap bang email va password.

Request body:

```json
{
  "email": "candidate@example.com",
  "password": "password123"
}
```

Response thanh cong: `200 OK`

```json
{
  "message": "login success",
  "data": {
    "user": {
      "id": 1,
      "email": "candidate@example.com",
      "fullname": "Nguyen Van A",
      "phone": "0901234567",
      "avatarUrl": "https://example.com/avatar.png"
    },
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
  }
}
```

### 4. Refresh token

`POST /api/auth/refresh-token`

Tao access token moi tu refresh token.

Request body:

```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
}
```

Validation chinh:

| Field | Rule |
|---|---|
| `refreshToken` | Bat buoc |

Response thanh cong: `200 OK`

```json
{
  "message": "refresh token success",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9..."
  }
}
```

### 5. Logout

`POST /api/auth/logout`

Dang xuat bang refresh token. Endpoint nay can access token hop le.

Request body:

```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
}
```

Validation chinh:

| Field | Rule |
|---|---|
| `refreshToken` | Bat buoc |

Response thanh cong: `200 OK`

```json
{
  "message": "logout success",
  "data": null
}
```

## Candidate API

Base path: `/api/candidates`

### 1. Tao candidate

`POST /api/candidates`

Tao ho so candidate moi.

Request body: giong `POST /api/auth/register`.

Response thanh cong: `201 Created`

```json
{
  "message": "create success",
  "data": {
    "id": 1,
    "user": {
      "id": 1,
      "email": "candidate@example.com",
      "fullname": "Nguyen Van A",
      "phone": "0901234567",
      "avatarUrl": "https://example.com/avatar.png"
    },
    "linkedinUrl": "https://linkedin.com/in/candidate",
    "githubUrl": "https://github.com/candidate",
    "portfolioUrl": "https://candidate.dev",
    "currentPosition": "Java Developer",
    "yearsOfExperience": 2,
    "createdAt": "2026-06-27T10:00:00Z",
    "updatedAt": "2026-06-27T10:00:00Z"
  }
}
```

### 2. Lay danh sach candidate

`GET /api/candidates`

Lay danh sach tat ca ung vien.

Request: khong co body.

Response thanh cong: `200 OK`

```json
{
  "message": "success",
  "data": [
    {
      "id": 1,
      "user": {
        "id": 1,
        "email": "candidate@example.com",
        "fullname": "Nguyen Van A",
        "phone": "0901234567",
        "avatarUrl": "https://example.com/avatar.png"
      },
      "linkedinUrl": "https://linkedin.com/in/candidate",
      "githubUrl": "https://github.com/candidate",
      "portfolioUrl": "https://candidate.dev",
      "currentPosition": "Java Developer",
      "yearsOfExperience": 2,
      "createdAt": "2026-06-27T10:00:00Z",
      "updatedAt": "2026-06-27T10:00:00Z"
    }
  ]
}
```

### 3. Lay profile candidate

`GET /api/candidates/profile`

Lay thong tin profile cua candidate theo user dang dang nhap. Code hien tai lay `userId` tu `Authentication`, nen request can access token hop le va khong can gui `id`.

Response thanh cong: `200 OK`

```json
{
  "message": "success",
  "data": {
    "id": 1,
    "user": {
      "id": 1,
      "email": "candidate@example.com",
      "fullname": "Nguyen Van A",
      "phone": "0901234567",
      "avatarUrl": "https://example.com/avatar.png"
    },
    "linkedinUrl": "https://linkedin.com/in/candidate",
    "githubUrl": "https://github.com/candidate",
    "portfolioUrl": "https://candidate.dev",
    "currentPosition": "Java Developer",
    "yearsOfExperience": 2,
    "createdAt": "2026-06-27T10:00:00Z",
    "updatedAt": "2026-06-27T10:00:00Z"
  }
}
```

### 4. Cap nhat candidate

`PATCH /api/candidates`

Cap nhat thong tin candidate cua user dang dang nhap. Code hien tai lay `userId` tu `Authentication`, nen request can access token hop le va khong can gui `id`.

Request body:

```json
{
  "fullName": "Nguyen Van A Updated",
  "email": "candidate@example.com",
  "phone": "0901234567",
  "password": "password123",
  "avatar": "https://example.com/avatar-new.png",
  "linkedinUrl": "https://linkedin.com/in/candidate",
  "githubUrl": "https://github.com/candidate",
  "portfolioUrl": "https://candidate.dev",
  "currentPosition": "Senior Java Developer",
  "yearsOfExperience": 4
}
```

Response thanh cong: `200 OK`

```json
{
  "message": "success",
  "data": {
    "id": 1,
    "user": {
      "id": 1,
      "email": "candidate@example.com",
      "fullname": "Nguyen Van A Updated",
      "phone": "0901234567",
      "avatarUrl": "https://example.com/avatar-new.png"
    },
    "linkedinUrl": "https://linkedin.com/in/candidate",
    "githubUrl": "https://github.com/candidate",
    "portfolioUrl": "https://candidate.dev",
    "currentPosition": "Senior Java Developer",
    "yearsOfExperience": 4,
    "createdAt": "2026-06-27T10:00:00Z",
    "updatedAt": "2026-06-27T10:05:00Z"
  }
}
```

## Recruiter API

Base path: `/api/recruiters`

### 1. Lay danh sach recruiter

`GET /api/recruiters`

Request: khong co body.

Response thanh cong: `200 OK`

```json
{
  "message": "success",
  "data": [
    {
      "id": 1,
      "user": {
        "id": 2,
        "email": "recruiter@example.com",
        "fullname": "Tran Thi B",
        "phone": "0912345678",
        "avatarUrl": "https://example.com/recruiter.png"
      },
      "company": {
        "id": 1,
        "name": "ABC Company",
        "logo": "https://example.com/logo.png",
        "email": "contact@abc.com",
        "website": "https://abc.com",
        "description": "Software company",
        "address": "Ho Chi Minh City",
        "createdAt": "2026-06-27T10:00:00Z",
        "updatedAt": "2026-06-27T10:00:00Z"
      },
      "position": "HR Manager"
    }
  ]
}
```

### 2. Lay recruiter theo userId

`GET /api/recruiters/{userId}`

Path variable:

| Param | Type | Mo ta |
|---|---|---|
| `userId` | `Long` | ID user cua recruiter |

Response thanh cong: `200 OK`

```json
{
  "message": "success",
  "data": {
    "id": 1,
    "user": {
      "id": 2,
      "email": "recruiter@example.com",
      "fullname": "Tran Thi B",
      "phone": "0912345678",
      "avatarUrl": "https://example.com/recruiter.png"
    },
    "company": {
      "id": 1,
      "name": "ABC Company",
      "logo": "https://example.com/logo.png",
      "email": "contact@abc.com",
      "website": "https://abc.com",
      "description": "Software company",
      "address": "Ho Chi Minh City",
      "createdAt": "2026-06-27T10:00:00Z",
      "updatedAt": "2026-06-27T10:00:00Z"
    },
    "position": "HR Manager"
  }
}
```

### 3. Cap nhat recruiter

`PATCH /api/recruiters/{id}`

Path variable:

| Param | Type | Mo ta |
|---|---|---|
| `id` | `Long` | ID recruiter |

Request body:

```json
{
  "fullName": "Tran Thi B Updated",
  "email": "recruiter@example.com",
  "phone": "0912345678",
  "password": "password123",
  "avatar": "https://example.com/recruiter-new.png",
  "companyId": 1,
  "position": "Senior HR Manager"
}
```

Response thanh cong: `200 OK`

```json
{
  "message": "success",
  "data": {
    "id": 1,
    "user": {
      "id": 2,
      "email": "recruiter@example.com",
      "fullname": "Tran Thi B Updated",
      "phone": "0912345678",
      "avatarUrl": "https://example.com/recruiter-new.png"
    },
    "company": {
      "id": 1,
      "name": "ABC Company",
      "logo": "https://example.com/logo.png",
      "email": "contact@abc.com",
      "website": "https://abc.com",
      "description": "Software company",
      "address": "Ho Chi Minh City",
      "createdAt": "2026-06-27T10:00:00Z",
      "updatedAt": "2026-06-27T10:00:00Z"
    },
    "position": "Senior HR Manager"
  }
}
```

## Company API

Base path: `/api/companies`

### 1. Tao company

`POST /api/companies`

Request body:

```json
{
  "name": "ABC Company",
  "logo": "https://example.com/logo.png",
  "email": "contact@abc.com",
  "website": "https://abc.com",
  "description": "Software company",
  "address": "Ho Chi Minh City"
}
```

Validation chinh:

| Field | Rule |
|---|---|
| `name` | Bat buoc, toi da 255 ky tu |
| `logo` | Toi da 255 ky tu |
| `email` | Bat buoc, dung dinh dang email, toi da 255 ky tu |
| `website` | Toi da 255 ky tu |
| `description` | Bat buoc, toi da 255 ky tu |
| `address` | Bat buoc, toi da 255 ky tu |

Response thanh cong: `201 Created`

```json
{
  "message": "create success",
  "data": {
    "id": 1,
    "name": "ABC Company",
    "logo": "https://example.com/logo.png",
    "email": "contact@abc.com",
    "website": "https://abc.com",
    "description": "Software company",
    "address": "Ho Chi Minh City",
    "createdAt": "2026-06-27T10:00:00Z",
    "updatedAt": "2026-06-27T10:00:00Z"
  }
}
```

### 2. Lay danh sach company

`GET /api/companies`

Request: khong co body.

Response thanh cong: `200 OK`

```json
{
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "ABC Company",
      "logo": "https://example.com/logo.png",
      "email": "contact@abc.com",
      "website": "https://abc.com",
      "description": "Software company",
      "address": "Ho Chi Minh City",
      "createdAt": "2026-06-27T10:00:00Z",
      "updatedAt": "2026-06-27T10:00:00Z"
    }
  ]
}
```

### 3. Lay company theo id

`GET /api/companies/{id}`

Path variable:

| Param | Type | Mo ta |
|---|---|---|
| `id` | `Long` | ID company |

Response thanh cong: `200 OK`

```json
{
  "message": "success",
  "data": {
    "id": 1,
    "name": "ABC Company",
    "logo": "https://example.com/logo.png",
    "email": "contact@abc.com",
    "website": "https://abc.com",
    "description": "Software company",
    "address": "Ho Chi Minh City",
    "createdAt": "2026-06-27T10:00:00Z",
    "updatedAt": "2026-06-27T10:00:00Z"
  }
}
```

### 4. Cap nhat company

`PATCH /api/companies/{id}`

Path variable:

| Param | Type | Mo ta |
|---|---|---|
| `id` | `Long` | ID company |

Request body:

```json
{
  "name": "ABC Company Updated",
  "logo": "https://example.com/logo-new.png",
  "email": "contact@abc.com",
  "website": "https://abc.com",
  "description": "Updated company description",
  "address": "Ha Noi"
}
```

Response thanh cong: `200 OK`

```json
{
  "message": "success",
  "data": {
    "id": 1,
    "name": "ABC Company Updated",
    "logo": "https://example.com/logo-new.png",
    "email": "contact@abc.com",
    "website": "https://abc.com",
    "description": "Updated company description",
    "address": "Ha Noi",
    "createdAt": "2026-06-27T10:00:00Z",
    "updatedAt": "2026-06-27T10:05:00Z"
  }
}
```

## User API

Base path: `/api/users`

### 1. Doi mat khau

`PATCH /api/users/password`

Doi mat khau user dang dang nhap. Code hien tai lay `userId` tu `Authentication`, nen request can access token hop le va khong can gui `id`.

Request body:

```json
{
  "oldPassword": "oldPassword123",
  "newPassword": "newPassword123",
  "confirmPassword": "newPassword123"
}
```

Validation chinh:

| Field | Rule |
|---|---|
| `oldPassword` | Bat buoc |
| `newPassword` | Bat buoc |
| `confirmPassword` | Bat buoc |

Response thanh cong: `200 OK`

```json
{
  "message": "success",
  "data": null
}
```

## File API

Base path: `/api/files`

### 1. Upload file

`POST /api/files/upload`

Upload file len server.

Request:

| Type | Field | Mo ta |
|---|---|---|
| `multipart/form-data` | `file` | File can upload |

Vi du curl:

```bash
curl -X POST http://localhost:8080/api/files/upload \
  -F "file=@resume.pdf"
```

Response thanh cong: `201 Created`

```json
{
  "message": "create file success",
  "data": "20260627100000-resume.pdf"
}
```

Ghi chu:

- API upload file tra ve `ApiResponse<String>`.
- `data` la ten file cuoi cung duoc luu vao database.
- Neu file empty, `data` tra ve chuoi rong `""`.

## Resume API

Base path: `/api/resumes`

### 1. Tao resume

`POST /api/resumes`

Request body:

```json
{
  "candidateId": 1,
  "fileName": "resume.pdf",
  "fileUrl": "http://localhost:8080/uploads/resume.pdf"
}
```

Validation chinh: `candidateId` bat buoc; `fileName`, `fileUrl` bat buoc va khong duoc blank.

Response thanh cong: `201 Created`

```json
{
  "message": "create success",
  "data": {
    "id": 1,
    "candidateId": 1,
    "fileName": "resume.pdf",
    "fileUrl": "http://localhost:8080/uploads/resume.pdf",
    "createdAt": "2026-06-27T10:00:00Z"
  }
}
```

### 2. Cac endpoint resume khac

| Method | Endpoint | Mo ta | Response |
|---|---|---|---|
| `GET` | `/api/resumes` | Lay tat ca resume | `ApiResponse<List<ResumeResponse>>` |
| `GET` | `/api/resumes/{id}` | Lay resume theo id | `ApiResponse<ResumeResponse>` |
| `GET` | `/api/resumes/candidate/{candidateId}` | Lay resume theo candidate | `ApiResponse<List<ResumeResponse>>` |
| `PATCH` | `/api/resumes/{id}` | Cap nhat resume voi body nhu tao resume | `ApiResponse<ResumeResponse>` |
| `DELETE` | `/api/resumes/{id}` | Xoa resume | `ApiResponse<Void>` voi message `delete success` |

## Job API

Base path: `/api/jobs`

### 1. Tao job

`POST /api/jobs`

Request body:

```json
{
  "title": "Java Developer",
  "description": "Build ATS features",
  "requirements": "Java, Spring Boot",
  "location": "Ho Chi Minh City",
  "employmentType": "Full-time",
  "salaryMin": 1000,
  "salaryMax": 2000,
  "status": "OPEN",
  "createdBy": 1
}
```

Enum `status`: `OPEN`, `CLOSED`.

Response thanh cong: `201 Created`

```json
{
  "message": "create success",
  "data": {
    "id": 1,
    "title": "Java Developer",
    "description": "Build ATS features",
    "requirements": "Java, Spring Boot",
    "location": "Ho Chi Minh City",
    "employmentType": "Full-time",
    "salaryMin": 1000,
    "salaryMax": 2000,
    "status": "OPEN",
    "createdBy": 1,
    "createdAt": "2026-06-27T10:00:00Z",
    "updatedAt": "2026-06-27T10:00:00Z"
  }
}
```

### 2. Cac endpoint job khac

| Method | Endpoint | Mo ta | Response |
|---|---|---|---|
| `GET` | `/api/jobs` | Lay tat ca job | `ApiResponse<List<JobResponse>>` |
| `GET` | `/api/jobs/{id}` | Lay job theo id | `ApiResponse<JobResponse>` |
| `PATCH` | `/api/jobs/{id}` | Cap nhat job voi body nhu tao job | `ApiResponse<JobResponse>` |
| `DELETE` | `/api/jobs/{id}` | Xoa job | `ApiResponse<Void>` voi message `delete success` |

## Job Application API

Base path: `/api/applications`

### 1. Tao application

`POST /api/applications`

Request body:

```json
{
  "candidateId": 1,
  "jobId": 1,
  "stageId": 1,
  "status": "APPLIED",
  "source": "LinkedIn",
  "expectedSalary": 1500,
  "note": "Available immediately"
}
```

Enum `status`: `APPLIED`, `SCREENING`, `INTERVIEW`, `OFFERED`, `HIRED`, `REJECTED`, `WITHDRAWN`.

Response thanh cong: `201 Created`

```json
{
  "message": "create success",
  "data": {
    "id": 1,
    "candidateId": 1,
    "jobId": 1,
    "stageId": 1,
    "status": "APPLIED",
    "source": "LinkedIn",
    "expectedSalary": 1500,
    "note": "Available immediately",
    "appliedAt": "2026-06-27T10:00:00Z",
    "updatedAt": "2026-06-27T10:00:00Z"
  }
}
```

### 2. Lay danh sach application

`GET /api/applications`

Query optional:

| Param | Mo ta |
|---|---|
| `candidateId` | Neu co thi loc theo candidate |
| `jobId` | Neu co thi loc theo job |

Response thanh cong: `ApiResponse<List<JobApplicationResponse>>` voi message `success`.

### 3. Cac endpoint application khac

| Method | Endpoint | Mo ta | Response |
|---|---|---|---|
| `GET` | `/api/applications/{id}` | Lay application theo id | `ApiResponse<JobApplicationResponse>` |
| `GET` | `/api/applications/candidate/{candidateId}` | Lay application theo candidate | `ApiResponse<List<JobApplicationResponse>>` |
| `GET` | `/api/applications/job/{jobId}` | Lay application theo job | `ApiResponse<List<JobApplicationResponse>>` |
| `PATCH` | `/api/applications/{id}` | Cap nhat application voi body nhu tao application | `ApiResponse<JobApplicationResponse>` |
| `PATCH` | `/api/applications/{id}/status` | Doi status voi body `status`, `stageId` | `ApiResponse<JobApplicationResponse>` |
| `DELETE` | `/api/applications/{id}` | Xoa application | `ApiResponse<Void>` voi message `delete success` |

Body doi status:

```json
{
  "status": "INTERVIEW",
  "stageId": 2
}
```

## Application Stage API

Base path: `/api/application-stages`

Request body tao/cap nhat:

```json
{
  "name": "Screening",
  "position": 1
}
```

Validation chinh: `name` bat buoc, toi da 100 ky tu; `position` bat buoc va lon hon hoac bang 0.

| Method | Endpoint | Mo ta | Response |
|---|---|---|---|
| `POST` | `/api/application-stages` | Tao stage | `ApiResponse<ApplicationStageResponse>` voi message `create success` |
| `GET` | `/api/application-stages` | Lay tat ca stage | `ApiResponse<List<ApplicationStageResponse>>` |
| `GET` | `/api/application-stages/{id}` | Lay stage theo id | `ApiResponse<ApplicationStageResponse>` |
| `PATCH` | `/api/application-stages/{id}` | Cap nhat stage | `ApiResponse<ApplicationStageResponse>` |
| `DELETE` | `/api/application-stages/{id}` | Xoa stage | `ApiResponse<Void>` voi message `delete success` |

## Interview API

Base path: `/api/interviews`

Request body tao/cap nhat:

```json
{
  "jobApplicationId": 1,
  "interviewerId": 1,
  "title": "Technical Interview",
  "interviewTime": "2026-06-30T03:00:00Z",
  "location": "Google Meet",
  "status": "SCHEDULED"
}
```

Enum `status`: `SCHEDULED`, `IN_PROGRESS`, `COMPLETED`, `CANCELLED`, `NO_SHOW`, `RESCHEDULED`.

| Method | Endpoint | Mo ta | Response |
|---|---|---|---|
| `POST` | `/api/interviews` | Tao interview | `ApiResponse<InterviewResponse>` voi message `create success` |
| `GET` | `/api/interviews` | Lay tat ca interview | `ApiResponse<List<InterviewResponse>>` |
| `GET` | `/api/interviews/{id}` | Lay interview theo id | `ApiResponse<InterviewResponse>` |
| `GET` | `/api/interviews/application/{applicationId}` | Lay interview theo application | `ApiResponse<List<InterviewResponse>>` |
| `PATCH` | `/api/interviews/{id}` | Cap nhat interview | `ApiResponse<InterviewResponse>` |
| `PATCH` | `/api/interviews/{id}/status` | Doi status voi body `status` | `ApiResponse<InterviewResponse>` |
| `DELETE` | `/api/interviews/{id}` | Xoa interview | `ApiResponse<Void>` voi message `delete success` |

Body doi status:

```json
{
  "status": "COMPLETED"
}
```

## Interview Feedback API

Base path: `/api/interview-feedbacks`

Request body tao/cap nhat:

```json
{
  "interviewId": 1,
  "recruiterId": 1,
  "rating": 4,
  "comment": "Strong technical foundation",
  "recommendation": "HIRE"
}
```

Validation chinh: `interviewId`, `recruiterId` bat buoc; `rating` tu 1 den 5; `recommendation` toi da 50 ky tu.

| Method | Endpoint | Mo ta | Response |
|---|---|---|---|
| `POST` | `/api/interview-feedbacks` | Tao feedback | `ApiResponse<InterviewFeedbackResponse>` voi message `create success` |
| `GET` | `/api/interview-feedbacks` | Lay tat ca feedback | `ApiResponse<List<InterviewFeedbackResponse>>` |
| `GET` | `/api/interview-feedbacks/{id}` | Lay feedback theo id | `ApiResponse<InterviewFeedbackResponse>` |
| `PATCH` | `/api/interview-feedbacks/{id}` | Cap nhat feedback | `ApiResponse<InterviewFeedbackResponse>` |
| `DELETE` | `/api/interview-feedbacks/{id}` | Xoa feedback | `ApiResponse<Void>` voi message `delete success` |

## Activity Log API

Base path: `/api/activity-logs`

Request body tao:

```json
{
  "userId": 1,
  "action": "CREATE",
  "entityType": "JOB",
  "entityId": 1,
  "description": "Created job Java Developer"
}
```

Enum `action`: `CREATE`, `UPDATE`, `DELETE`, `CHANGE_STATUS`.

Enum `entityType`: `JOB`, `APPLICATION`, `INTERVIEW`, `CANDIDATE`, `USER`.

| Method | Endpoint | Mo ta | Response |
|---|---|---|---|
| `POST` | `/api/activity-logs` | Tao activity log | `ApiResponse<ActivityLogResponse>` voi message `create success` |
| `GET` | `/api/activity-logs` | Lay tat ca activity log | `ApiResponse<List<ActivityLogResponse>>` |
| `GET` | `/api/activity-logs/{id}` | Lay activity log theo id | `ApiResponse<ActivityLogResponse>` |
| `GET` | `/api/activity-logs/user/{userId}` | Lay activity log theo user | `ApiResponse<List<ActivityLogResponse>>` |
| `DELETE` | `/api/activity-logs/{id}` | Xoa activity log | `ApiResponse<Void>` voi message `delete success` |

## Tom tat endpoint

| Method | Endpoint | Mo ta ngan |
|---|---|---|
| `POST` | `/api/auth/register` | Dang ky candidate |
| `POST` | `/api/auth/register/recruiter` | Dang ky recruiter |
| `POST` | `/api/auth/login` | Dang nhap |
| `POST` | `/api/auth/refresh-token` | Lay access token moi |
| `POST` | `/api/auth/logout` | Dang xuat |
| `POST` | `/api/candidates` | Tao candidate |
| `GET` | `/api/candidates` | Lay danh sach candidate |
| `GET` | `/api/candidates/profile` | Lay profile candidate |
| `PATCH` | `/api/candidates` | Cap nhat candidate |
| `GET` | `/api/recruiters` | Lay danh sach recruiter |
| `GET` | `/api/recruiters/{userId}` | Lay recruiter theo userId |
| `PATCH` | `/api/recruiters/{id}` | Cap nhat recruiter |
| `POST` | `/api/companies` | Tao company |
| `GET` | `/api/companies` | Lay danh sach company |
| `GET` | `/api/companies/{id}` | Lay company theo id |
| `PATCH` | `/api/companies/{id}` | Cap nhat company |
| `PATCH` | `/api/users/password` | Doi mat khau |
| `POST` | `/api/files/upload` | Upload file |
| `POST` | `/api/resumes` | Tao resume |
| `GET` | `/api/resumes` | Lay danh sach resume |
| `GET` | `/api/resumes/{id}` | Lay resume theo id |
| `GET` | `/api/resumes/candidate/{candidateId}` | Lay resume theo candidate |
| `PATCH` | `/api/resumes/{id}` | Cap nhat resume |
| `DELETE` | `/api/resumes/{id}` | Xoa resume |
| `POST` | `/api/jobs` | Tao job |
| `GET` | `/api/jobs` | Lay danh sach job |
| `GET` | `/api/jobs/{id}` | Lay job theo id |
| `PATCH` | `/api/jobs/{id}` | Cap nhat job |
| `DELETE` | `/api/jobs/{id}` | Xoa job |
| `POST` | `/api/applications` | Tao application |
| `GET` | `/api/applications` | Lay/loc danh sach application |
| `GET` | `/api/applications/{id}` | Lay application theo id |
| `GET` | `/api/applications/candidate/{candidateId}` | Lay application theo candidate |
| `GET` | `/api/applications/job/{jobId}` | Lay application theo job |
| `PATCH` | `/api/applications/{id}` | Cap nhat application |
| `PATCH` | `/api/applications/{id}/status` | Doi trang thai application |
| `DELETE` | `/api/applications/{id}` | Xoa application |
| `POST` | `/api/application-stages` | Tao stage |
| `GET` | `/api/application-stages` | Lay danh sach stage |
| `GET` | `/api/application-stages/{id}` | Lay stage theo id |
| `PATCH` | `/api/application-stages/{id}` | Cap nhat stage |
| `DELETE` | `/api/application-stages/{id}` | Xoa stage |
| `POST` | `/api/interviews` | Tao interview |
| `GET` | `/api/interviews` | Lay danh sach interview |
| `GET` | `/api/interviews/{id}` | Lay interview theo id |
| `GET` | `/api/interviews/application/{applicationId}` | Lay interview theo application |
| `PATCH` | `/api/interviews/{id}` | Cap nhat interview |
| `PATCH` | `/api/interviews/{id}/status` | Doi trang thai interview |
| `DELETE` | `/api/interviews/{id}` | Xoa interview |
| `POST` | `/api/interview-feedbacks` | Tao interview feedback |
| `GET` | `/api/interview-feedbacks` | Lay danh sach interview feedback |
| `GET` | `/api/interview-feedbacks/{id}` | Lay interview feedback theo id |
| `PATCH` | `/api/interview-feedbacks/{id}` | Cap nhat interview feedback |
| `DELETE` | `/api/interview-feedbacks/{id}` | Xoa interview feedback |
| `POST` | `/api/activity-logs` | Tao activity log |
| `GET` | `/api/activity-logs` | Lay danh sach activity log |
| `GET` | `/api/activity-logs/{id}` | Lay activity log theo id |
| `GET` | `/api/activity-logs/user/{userId}` | Lay activity log theo user |
| `DELETE` | `/api/activity-logs/{id}` | Xoa activity log |
