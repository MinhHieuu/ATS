package com.example.ats.infrastructure.persistence.repository;

import com.example.ats.domain.model.AuditAction;
import com.example.ats.domain.model.AuditEntityType;
import com.example.ats.infrastructure.persistence.entity.AuditLogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;

public interface SpringDataAuditLogRepository extends JpaRepository<AuditLogEntity, Long> {

    @Override
    @EntityGraph(attributePaths = "actor")
    Optional<AuditLogEntity> findById(Long id);

    // Count query duoc Spring Data tu suy ra tu cau tren -> khong copy tay de tranh lech predicate.
    @EntityGraph(attributePaths = "actor")
    @Query("select a from AuditLogEntity a where "
            + "(:actorId is null or a.actorId = :actorId) and "
            + "(:entityType is null or a.entityType = :entityType) and "
            + "(:action is null or a.action = :action) and "
            + "(:from is null or a.createdAt >= :from) and "
            + "(:to is null or a.createdAt <= :to)")
    Page<AuditLogEntity> search(@Param("actorId") Long actorId,
                                @Param("entityType") AuditEntityType entityType,
                                @Param("action") AuditAction action,
                                @Param("from") Instant from,
                                @Param("to") Instant to,
                                Pageable pageable);

    @EntityGraph(attributePaths = "actor")
    Page<AuditLogEntity> findByEntityTypeAndEntityId(AuditEntityType entityType, Long entityId, Pageable pageable);
}
