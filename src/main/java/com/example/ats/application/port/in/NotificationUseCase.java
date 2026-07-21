package com.example.ats.application.port.in;

import com.example.ats.application.dto.response.NotificationResponse;
import com.example.ats.domain.model.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationUseCase {
    void send(Long recipientId, NotificationType type, String title, String content, Long referenceId);
    void sendToAdmins(NotificationType type, String title, String content, Long referenceId, Long excludeUserId);
    Page<NotificationResponse> findByRecipient(Long recipientId, Pageable pageable);
    NotificationResponse markAsRead(Long id, Long recipientId);
    void markAllAsRead(Long recipientId);
    long countUnread(Long recipientId);
}
