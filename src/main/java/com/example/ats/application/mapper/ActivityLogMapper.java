package com.example.ats.application.mapper;

import com.example.ats.application.dto.response.ActivityLogResponse;
import com.example.ats.domain.model.ActivityLog;
import com.example.ats.infrastructure.persistence.entity.ActivityLogEntity;
import org.springframework.stereotype.Component;

@Component
public class ActivityLogMapper {
    public ActivityLogResponse toResponse(ActivityLog log) {
        return log == null ? null : new ActivityLogResponse(log.getId(), log.getUserId(), log.getAction(),
                log.getEntityType(), log.getEntityId(), log.getDescription(), log.getCreatedAt());
    }

    public ActivityLog toDomain(ActivityLogEntity entity) {
        if (entity == null) {
            return null;
        }
        Long userId = entity.getUser() == null ? null : entity.getUser().getId();
        return new ActivityLog(entity.getId(), userId, entity.getAction(),
                entity.getEntityType(), entity.getEntityId(), entity.getDescription(), entity.getCreatedAt());
    }
}
