package com.example.ats.domain.model;

public enum AuditAction {
    // Quản lý người dùng
    USER_CREATED,
    USER_UPDATED,
    USER_ACTIVATED,
    USER_DEACTIVATED,
    USER_ROLE_CHANGED, // Dành sẵn — hiện chưa có endpoint đổi role sau khi tạo user

    // Quản lý ứng viên / hồ sơ
    CANDIDATE_CREATED,
    CANDIDATE_UPDATED,
    APPLICATION_CREATED,
    APPLICATION_STATUS_CHANGED,
    APPLICATION_DELETED,

    // Quản lý tin tuyển dụng
    JOB_CREATED,
    JOB_UPDATED,
    JOB_STATUS_CHANGED,
    JOB_DELETED
}
