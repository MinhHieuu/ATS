package com.example.ats.application.port.out;

import com.example.ats.domain.model.AuditAction;
import com.example.ats.domain.model.AuditEntityType;
import com.example.ats.domain.model.AuditLog;
import com.example.ats.domain.view.AuditLogView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;

public interface AuditLogRepository {
    AuditLog save(AuditLog log);
    Page<AuditLogView> search(Long actorId, AuditEntityType entityType, AuditAction action,
                              Instant from, Instant to, Pageable pageable);
    AuditLogView findById(Long id);
    Page<AuditLogView> findByEntity(AuditEntityType entityType, Long entityId, Pageable pageable);
}
