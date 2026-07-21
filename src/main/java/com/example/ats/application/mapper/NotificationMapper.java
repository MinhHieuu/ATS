package com.example.ats.application.mapper;

import com.example.ats.application.dto.response.NotificationResponse;
import com.example.ats.domain.model.Notification;
import com.example.ats.infrastructure.persistence.entity.NotificationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    NotificationResponse toResponse(Notification notification);

    @Mapping(source = "recipient.id", target = "recipientId")
    Notification toDomain(NotificationEntity entity);
}
