package com.example.ats.application.port.out;

import com.example.ats.domain.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationRepository {
    Notification save(Notification notification);
    Notification findByIdAndRecipient(Long id, Long recipientId);
    Page<Notification> findByRecipient(Long recipientId, Pageable pageable);
    long countUnreadByRecipient(Long recipientId);
    void markAllAsRead(Long recipientId);
}
