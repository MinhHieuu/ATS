package com.example.ats.domain.model;

import java.time.Instant;

public record ActivityLog(
    Long id,
    Long userId,
    ActivityAction action,
    EntityType entityType,
    Long entityId,
    String description,
    Instant createdAt
) {
    public ActivityLog changeActivityLog(ActivityAction action, EntityType entityType) {
        return new ActivityLog(
            id, userId, action, entityType, entityId, description, createdAt
        );
    }
} 