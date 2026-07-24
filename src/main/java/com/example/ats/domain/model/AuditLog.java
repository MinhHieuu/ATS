package com.example.ats.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class AuditLog {
    private Long id;
    private AuditAction action;
    private AuditEntityType entityType;
    private Long entityId;
    private Long actorId;
    private Role actorRole;
    private String oldValue;
    private String newValue;
    private String description;
    private Instant createdAt;
}
