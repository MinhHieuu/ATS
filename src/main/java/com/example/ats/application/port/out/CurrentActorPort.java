package com.example.ats.application.port.out;

import com.example.ats.domain.model.Role;

/**
 * Cung cấp thông tin người dùng đang thực hiện request hiện tại (từ SecurityContext).
 * Trả về null khi không có ai đăng nhập (ví dụ đăng ký công khai).
 */
public interface CurrentActorPort {
    Long currentUserId();
    Role currentRole();
}
