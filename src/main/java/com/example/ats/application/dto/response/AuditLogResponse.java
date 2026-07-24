package com.example.ats.application.dto.response;

import com.example.ats.domain.model.AuditAction;
import com.example.ats.domain.model.AuditEntityType;
import com.example.ats.domain.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogResponse {
    private Long id;
    private AuditAction action;
    private AuditEntityType entityType;
    private Long entityId;
    private Long actorId;
    private String actorName;
    private Role actorRole;
    private String oldValue;
    private String newValue;
    private String description;
    private Instant createdAt;
}
