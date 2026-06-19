package com.example.ats.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

import com.example.ats.domain.model.ActivityAction;
import com.example.ats.domain.model.EntityType;

@Entity
@Table(name = "activity_logs")
@Getter
@Setter
public class ActivityLogEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;
    @Enumerated(EnumType.STRING)
    @Column(name = "action")
    private ActivityAction action;
    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type")
    private EntityType entityType;
    @Column(name = "entity_id")
    private Long entityId;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

}
