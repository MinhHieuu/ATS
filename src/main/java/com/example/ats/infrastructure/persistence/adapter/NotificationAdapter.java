package com.example.ats.infrastructure.persistence.adapter;

import com.example.ats.application.mapper.NotificationMapper;
import com.example.ats.application.port.out.NotificationRepository;
import com.example.ats.domain.exception.ResourceNotFoundException;
import com.example.ats.domain.model.Notification;
import com.example.ats.infrastructure.persistence.entity.NotificationEntity;
import com.example.ats.infrastructure.persistence.entity.UserEntity;
import com.example.ats.infrastructure.persistence.repository.SpringDataNotificationRepository;
import com.example.ats.infrastructure.persistence.repository.SpringDataUserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

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
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + notification.getRecipientId()));
        NotificationEntity entity = new NotificationEntity(notification.getId(), recipient,
                notification.getType(), notification.getTitle(), notification.getMessage(),
                notification.getRelatedApplicationId(), notification.getRelatedJobId(),
                notification.getRead(), notification.getCreatedAt());
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public Notification findById(Long id) {
        return mapper.toDomain(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found")));
    }

    @Override
    public List<Notification> findByRecipient(Long recipientId) {
        return repository.findByRecipient_IdOrderByCreatedAtDesc(recipientId).stream()
                .map(mapper::toDomain)
                .toList();
    }
}
