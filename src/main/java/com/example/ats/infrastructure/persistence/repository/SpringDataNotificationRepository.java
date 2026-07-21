package com.example.ats.infrastructure.persistence.repository;

import com.example.ats.infrastructure.persistence.entity.NotificationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringDataNotificationRepository extends JpaRepository<NotificationEntity, Long> {

    Page<NotificationEntity> findByRecipient_IdOrderByCreatedAtDesc(Long recipientId, Pageable pageable);

    long countByRecipient_IdAndReadFalse(Long recipientId);

    @Modifying
    @Query("UPDATE NotificationEntity n SET n.read = true WHERE n.recipient.id = :recipientId AND n.read = false")
    void markAllAsReadByRecipientId(@Param("recipientId") Long recipientId);
}
