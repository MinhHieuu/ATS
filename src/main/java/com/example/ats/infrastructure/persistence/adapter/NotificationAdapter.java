package com.example.ats.infrastructure.persistence.adapter;

import com.example.ats.application.mapper.NotificationMapper;
import com.example.ats.application.port.out.NotificationRepository;
import com.example.ats.domain.exception.ResourceNotFoundException;
import com.example.ats.domain.model.Notification;
import com.example.ats.infrastructure.persistence.entity.NotificationEntity;
import com.example.ats.infrastructure.persistence.entity.UserEntity;
import com.example.ats.infrastructure.persistence.repository.SpringDataNotificationRepository;
import com.example.ats.infrastructure.persistence.repository.SpringDataUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class NotificationAdapter implements NotificationRepository {
    private final SpringDataNotificationRepository repository;
    private final SpringDataUserRepository userRepository;
    private final NotificationMapper mapper;

    public NotificationAdapter(SpringDataNotificationRepository repository,
                               SpringDataUserRepository userRepository,
                               NotificationMapper mapper) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public Notification save(Notification notification) {
        UserEntity recipient = userRepository.findById(notification.getRecipientId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        NotificationEntity entity = new NotificationEntity(notification.getId(), recipient,
                notification.getType(), notification.getTitle(), notification.getContent(),
                notification.getReferenceId(), notification.getRead(), notification.getCreatedAt());
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public Notification findByIdAndRecipient(Long id, Long recipientId) {
        NotificationEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        if (!entity.getRecipient().getId().equals(recipientId)) {
            throw new ResourceNotFoundException("Notification not found");
        }
        return mapper.toDomain(entity);
    }

    @Override
    public Page<Notification> findByRecipient(Long recipientId, Pageable pageable) {
        return repository.findByRecipient_IdOrderByCreatedAtDesc(recipientId, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public long countUnreadByRecipient(Long recipientId) {
        return repository.countByRecipient_IdAndReadFalse(recipientId);
    }

    @Override
    public void markAllAsRead(Long recipientId) {
        repository.markAllAsReadByRecipientId(recipientId);
    }
}
