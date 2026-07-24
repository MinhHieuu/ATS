package com.example.ats.infrastructure.persistence.entity;

import com.example.ats.domain.model.AuditAction;
import com.example.ats.domain.model.AuditEntityType;
import com.example.ats.domain.model.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "audit_logs", indexes = {
        @Index(name = "idx_audit_entity", columnList = "entity_type, entity_id"),
        @Index(name = "idx_audit_actor", columnList = "actor_id"),
        @Index(name = "idx_audit_created_at", columnList = "created_at")
})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuditLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private AuditAction action;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type", nullable = false, length = 20)
    private AuditEntityType entityType;

    @Column(name = "entity_id")
    private Long entityId;

    @Column(name = "actor_id")
    private Long actorId;

    @Enumerated(EnumType.STRING)
    @Column(name = "actor_role", length = 20)
    private Role actorRole;

    @Column(name = "old_value", length = 500)
    private String oldValue;

    @Column(name = "new_value", length = 500)
    private String newValue;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    // Quan hệ chỉ-đọc tới user để lấy tên actor khi hiển thị log.
    // Dùng chung cột actor_id (insertable/updatable=false) nên không ảnh hưởng lúc ghi.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_id", insertable = false, updatable = false)
    private UserEntity actor;

    public AuditLogEntity(Long id, AuditAction action, AuditEntityType entityType, Long entityId,
                          Long actorId, Role actorRole, String oldValue, String newValue,
                          String description, Instant createdAt) {
        this.id = id;
        this.action = action;
        this.entityType = entityType;
        this.entityId = entityId;
        this.actorId = actorId;
        this.actorRole = actorRole;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.description = description;
        this.createdAt = createdAt;
    }
}
