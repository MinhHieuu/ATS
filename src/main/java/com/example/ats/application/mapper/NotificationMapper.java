package com.example.ats.application.mapper;

import com.example.ats.application.dto.response.NotificationResponse;
import com.example.ats.domain.model.Notification;
import com.example.ats.infrastructure.persistence.entity.NotificationEntity;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {
    public NotificationResponse toResponse(Notification notification) {
        if (notification == null) {
            return null;
        }
        return new NotificationResponse(notification.getId(), notification.getRecipientId(),
                notification.getType(), notification.getTitle(), notification.getMessage(),
                notification.getRelatedApplicationId(), notification.getRelatedJobId(),
                notification.getRead(), notification.getCreatedAt());
    }

    public Notification toDomain(NotificationEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Notification(entity.getId(), entity.getRecipient().getId(), entity.getType(),
                entity.getTitle(), entity.getMessage(), entity.getRelatedApplicationId(),
                entity.getRelatedJobId(), entity.getRead(), entity.getCreatedAt());
    }
}
