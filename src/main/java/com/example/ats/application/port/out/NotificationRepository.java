package com.example.ats.application.port.out;

import com.example.ats.domain.model.Notification;

import java.util.List;

public interface NotificationRepository {
    Notification save(Notification notification);
    Notification findById(Long id);
    List<Notification> findByRecipient(Long recipientId);
}
