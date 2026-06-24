package com.example.ats.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class ActivityLog {
    private Long id;
    private Long userId;
    private ActivityAction action;
    private EntityType entityType;
    private Long entityId;
    private String description;
    private Instant createdAt;

    public ActivityLog changeActivityLog(ActivityAction action, EntityType entityType) {
        return new ActivityLog(
            id, userId, action, entityType, entityId, description, createdAt
        );
    }
} 
