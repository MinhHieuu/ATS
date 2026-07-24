package com.example.ats.application.port.in;

import com.example.ats.application.dto.response.AuditLogResponse;
import com.example.ats.domain.model.AuditAction;
import com.example.ats.domain.model.AuditEntityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;

public interface AuditLogUseCase {
    // ── Ghi log ────────────────────────────────────────────────
    void log(AuditAction action, AuditEntityType entityType, Long entityId,
             String description, String oldValue, String newValue);

    default void log(AuditAction action, AuditEntityType entityType, Long entityId, String description) {
        log(action, entityType, entityId, description, null, null);
    }

    // ── Đọc log (chỉ admin) ────────────────────────────────────
    Page<AuditLogResponse> search(Long actorId, AuditEntityType entityType, AuditAction action,
                                  Instant from, Instant to, Pageable pageable);
    AuditLogResponse findById(Long id);
    Page<AuditLogResponse> findByEntity(AuditEntityType entityType, Long entityId, Pageable pageable);
}
