package com.example.ats.infrastructure.persistence.adapter;

import com.example.ats.application.port.out.AuditLogRepository;
import com.example.ats.domain.exception.ResourceNotFoundException;
import com.example.ats.domain.model.AuditAction;
import com.example.ats.domain.model.AuditEntityType;
import com.example.ats.domain.model.AuditLog;
import com.example.ats.domain.view.AuditLogView;
import com.example.ats.infrastructure.persistence.entity.AuditLogEntity;
import com.example.ats.infrastructure.persistence.repository.SpringDataAuditLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public class AuditLogAdapter implements AuditLogRepository {
    private final SpringDataAuditLogRepository repository;

    public AuditLogAdapter(SpringDataAuditLogRepository repository) {
        this.repository = repository;
    }

    @Override
    public AuditLog save(AuditLog log) {
        AuditLogEntity entity = new AuditLogEntity(log.getId(), log.getAction(), log.getEntityType(),
                log.getEntityId(), log.getActorId(), log.getActorRole(),
                log.getOldValue(), log.getNewValue(), log.getDescription(), log.getCreatedAt());
        return toDomain(repository.save(entity));
    }

    @Override
    public Page<AuditLogView> search(Long actorId, AuditEntityType entityType, AuditAction action,
                                     Instant from, Instant to, Pageable pageable) {
        return repository.search(actorId, entityType, action, from, to, pageable).map(this::toView);
    }

    @Override
    public AuditLogView findById(Long id) {
        return toView(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Audit log not found")));
    }

    @Override
    public Page<AuditLogView> findByEntity(AuditEntityType entityType, Long entityId, Pageable pageable) {
        return repository.findByEntityTypeAndEntityId(entityType, entityId, pageable).map(this::toView);
    }

    private AuditLogView toView(AuditLogEntity entity) {
        String actorName = entity.getActor() != null ? entity.getActor().getFullname() : null;
        return new AuditLogView(toDomain(entity), actorName);
    }

    private AuditLog toDomain(AuditLogEntity entity) {
        return new AuditLog(entity.getId(), entity.getAction(), entity.getEntityType(),
                entity.getEntityId(), entity.getActorId(), entity.getActorRole(),
                entity.getOldValue(), entity.getNewValue(), entity.getDescription(), entity.getCreatedAt());
    }
}
