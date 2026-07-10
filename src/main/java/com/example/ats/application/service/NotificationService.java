package com.example.ats.application.service;

import com.example.ats.application.dto.response.NotificationResponse;
import com.example.ats.application.mapper.NotificationMapper;
import com.example.ats.application.port.in.NotificationUseCase;
import com.example.ats.application.port.out.NotificationRepository;
import com.example.ats.domain.exception.BusinessRuleException;
import com.example.ats.domain.model.ApplicationStatus;
import com.example.ats.domain.model.Job;
import com.example.ats.domain.model.JobApplication;
import com.example.ats.domain.model.Notification;
import com.example.ats.domain.model.NotificationType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class NotificationService implements NotificationUseCase {
    private final NotificationRepository repository;
    private final NotificationMapper mapper;
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(NotificationRepository repository, NotificationMapper mapper,
                               SimpMessagingTemplate messagingTemplate) {
        this.repository = repository;
        this.mapper = mapper;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public NotificationResponse notifyApplicationCreated(Job job, JobApplication application) {
        if (job.getCreatedBy() == null) {
            return null;
        }
        return createAndSend(new Notification(null, job.getCreatedBy(),
                NotificationType.JOB_APPLICATION_CREATED,
                "Có ứng viên mới",
                "Một ứng viên vừa ứng tuyển vào job " + job.getTitle(),
                application.getId(), job.getId(), false, Instant.now()));
    }

    @Override
    public NotificationResponse notifyApplicationStatusChanged(Long candidateUserId, Job job,
                                                              JobApplication application,
                                                              ApplicationStatus oldStatus) {
        return createAndSend(new Notification(null, candidateUserId,
                NotificationType.APPLICATION_STATUS_CHANGED,
                "Trạng thái ứng tuyển đã thay đổi",
                "Đơn ứng tuyển job " + job.getTitle() + " đã chuyển từ "
                        + oldStatus + " sang " + application.getStatus(),
                application.getId(), job.getId(), false, Instant.now()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> findByRecipient(Long recipientId) {
        return repository.findByRecipient(recipientId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public NotificationResponse markAsRead(Long id, Long recipientId) {
        Notification notification = repository.findById(id);
        if (!notification.getRecipientId().equals(recipientId)) {
            throw new BusinessRuleException("Cannot update another user's notification");
        }
        notification.setRead(true);
        return mapper.toResponse(repository.save(notification));
    }

    private NotificationResponse createAndSend(Notification notification) {
        NotificationResponse response = mapper.toResponse(repository.save(notification));
        messagingTemplate.convertAndSend(destination(notification.getRecipientId()), response);
        return response;
    }

    private String destination(Long recipientId) {
        return "/topic/users/" + recipientId + "/notifications";
    }
}
