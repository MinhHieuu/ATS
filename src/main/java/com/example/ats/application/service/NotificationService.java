package com.example.ats.application.service;

import com.example.ats.application.dto.response.NotificationResponse;
import com.example.ats.application.mapper.NotificationMapper;
import com.example.ats.application.port.in.NotificationUseCase;
import com.example.ats.application.port.out.NotificationRepository;
import com.example.ats.application.port.out.UserRepository;
import com.example.ats.domain.model.Notification;
import com.example.ats.domain.model.NotificationType;
import com.example.ats.domain.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class NotificationService implements NotificationUseCase {
    private final NotificationRepository repository;
    private final UserRepository userRepository;
    private final NotificationMapper mapper;
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(NotificationRepository repository, UserRepository userRepository,
                               NotificationMapper mapper, SimpMessagingTemplate messagingTemplate) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void send(Long recipientId, NotificationType type, String title, String content, Long referenceId) {
        Notification notification = new Notification(null, recipientId, type, title, content,
                referenceId, false, Instant.now());
        Notification saved = repository.save(notification);
        NotificationResponse response = mapper.toResponse(saved);
        messagingTemplate.convertAndSendToUser(
                recipientId.toString(), "/queue/notifications", response);
    }

    @Override
    public void sendToAdmins(NotificationType type, String title, String content, Long referenceId,
                             Long excludeUserId) {
        List<Long> adminIds = userRepository.findIdsByRole(Role.ADMIN);
        for (Long adminId : adminIds) {
            if (!adminId.equals(excludeUserId)) {
                send(adminId, type, title, content, referenceId);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponse> findByRecipient(Long recipientId, Pageable pageable) {
        return repository.findByRecipient(recipientId, pageable).map(mapper::toResponse);
    }

    @Override
    public NotificationResponse markAsRead(Long id, Long recipientId) {
        Notification notification = repository.findByIdAndRecipient(id, recipientId);
        notification.setRead(true);
        return mapper.toResponse(repository.save(notification));
    }

    @Override
    public void markAllAsRead(Long recipientId) {
        repository.markAllAsRead(recipientId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countUnread(Long recipientId) {
        return repository.countUnreadByRecipient(recipientId);
    }
}
