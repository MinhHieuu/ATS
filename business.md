# Business Workflow

## Auth Workflow

1. User dang ky tai khoan qua `/api/auth/register` hoac `/api/auth/register/recruiter`.
2. User dang nhap qua `/api/auth/login` bang email va password.
3. Neu thong tin dang nhap hop le, he thong cap:
   - `accessToken`: dung de goi cac API duoc bao ve.
   - `refreshToken`: dung de xin access token moi khi access token het han.
4. Client gui moi request den API duoc bao ve kem header:

```http
Authorization: Bearer <accessToken>
```

5. He thong kiem tra access token:
   - Token phai dung chu ky.
   - Token chua het han.
   - Token phai co type la `ACCESS`.
   - User trong token phai ton tai va dang active.
   - Role trong token phai khop voi role hien tai cua user.
6. Neu token hop le, request duoc thuc hien.
7. Neu token khong hop le, thieu token, token het han, hoac user khong con active, he thong tra ve `401 Unauthorized`; client can yeu cau user dang nhap lai hoac goi refresh token neu phu hop.
8. Khi access token het han, client goi `/api/auth/refresh-token` voi `refreshToken`.
9. He thong kiem tra refresh token:
   - Token phai dung chu ky.
   - Token chua het han.
   - Token phai co type la `REFRESH`.
   - Token phai con active trong he thong.
   - User phai ton tai va dang active.
10. Neu refresh token hop le, he thong cap access token moi.
11. Khi logout, client goi `/api/auth/logout` voi `refreshToken`; he thong revoke refresh token do.

## Candidate Apply Workflow

1. Candidate apply vao mot job dang mo.
2. He thong tao moi application voi trang thai ban dau la `Application Created`.
3. Application di qua cac business stage:

```text
Candidate Apply
        ↓
Application Created
        ↓
Screening
        ↓
Interview
        ↓
Offer
        ↓
Hired / Rejected
```

4. Y nghia tung stage:
   - `Candidate Apply`: Ung vien nop don vao job.
   - `Application Created`: He thong ghi nhan application moi.
   - `Screening`: Nha tuyen dung xem xet ho so, CV, kinh nghiem va thong tin ung vien.
   - `Interview`: Ung vien duoc moi phong van va ghi nhan ket qua phong van.
   - `Offer`: Nha tuyen dung gui offer neu ung vien dat yeu cau.
   - `Hired`: Ung vien chap nhan offer va duoc tuyen.
   - `Rejected`: Ung vien bi loai o bat ky buoc nao trong quy trinh.

5. Application co the ket thuc tai:
   - `Hired`: Quy trinh thanh cong.
   - `Rejected`: Quy trinh dung lai do ung vien khong phu hop, tu choi offer, hoac nha tuyen dung huy application.
