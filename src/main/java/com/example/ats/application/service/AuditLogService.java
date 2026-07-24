package com.example.ats.application.service;

import com.example.ats.application.dto.response.AuditLogResponse;
import com.example.ats.application.mapper.AuditLogMapper;
import com.example.ats.application.port.in.AuditLogUseCase;
import com.example.ats.application.port.out.AuditLogRepository;
import com.example.ats.application.port.out.CurrentActorPort;
import com.example.ats.domain.model.AuditAction;
import com.example.ats.domain.model.AuditEntityType;
import com.example.ats.domain.model.AuditLog;
import com.example.ats.domain.model.Role;
import com.example.ats.domain.view.AuditLogView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Transactional
public class AuditLogService implements AuditLogUseCase {
    private final AuditLogRepository repository;
    private final CurrentActorPort currentActor;
    private final AuditLogMapper mapper;

    public AuditLogService(AuditLogRepository repository, CurrentActorPort currentActor, AuditLogMapper mapper) {
        this.repository = repository;
        this.currentActor = currentActor;
        this.mapper = mapper;
    }

    // Khop voi @Column(length = 500) cua old_value / new_value trong AuditLogEntity.
    private static final int VALUE_MAX_LENGTH = 500;

    @Override
    public void log(AuditAction action, AuditEntityType entityType, Long entityId,
                    String description, String oldValue, String newValue) {
        Long actorId = currentActor.currentUserId();
        Role actorRole = currentActor.currentRole();
        // Cat bot phong ho: mot dong log qua dai khong duoc phep lam rollback hanh dong nghiep vu.
        repository.save(new AuditLog(null, action, entityType, entityId,
                actorId, actorRole, truncate(oldValue), truncate(newValue), description, Instant.now()));
    }

    private String truncate(String value) {
        if (value == null || value.length() <= VALUE_MAX_LENGTH) {
            return value;
        }
        return value.substring(0, VALUE_MAX_LENGTH);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditLogResponse> search(Long actorId, AuditEntityType entityType, AuditAction action,
                                         Instant from, Instant to, Pageable pageable) {
        return repository.search(actorId, entityType, action, from, to, pageable).map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public AuditLogResponse findById(Long id) {
        return toResponse(repository.findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditLogResponse> findByEntity(AuditEntityType entityType, Long entityId, Pageable pageable) {
        return repository.findByEntity(entityType, entityId, pageable).map(this::toResponse);
    }

    private AuditLogResponse toResponse(AuditLogView view) {
        AuditLogResponse response = mapper.toResponse(view.log());
        response.setActorName(view.actorName());
        return response;
    }
}
