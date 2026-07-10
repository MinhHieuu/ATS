package com.example.ats.infrastructure.persistence.repository;

import com.example.ats.infrastructure.persistence.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataNotificationRepository extends JpaRepository<NotificationEntity, Long> {
    List<NotificationEntity> findByRecipient_IdOrderByCreatedAtDesc(Long recipientId);
}
