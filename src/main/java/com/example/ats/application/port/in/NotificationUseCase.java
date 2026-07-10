package com.example.ats.application.port.in;

import com.example.ats.application.dto.response.NotificationResponse;
import com.example.ats.domain.model.ApplicationStatus;
import com.example.ats.domain.model.Job;
import com.example.ats.domain.model.JobApplication;

import java.util.List;

public interface NotificationUseCase {
    NotificationResponse notifyApplicationCreated(Job job, JobApplication application);
    NotificationResponse notifyApplicationStatusChanged(Long candidateUserId, Job job,
                                                        JobApplication application,
                                                        ApplicationStatus oldStatus);
    List<NotificationResponse> findByRecipient(Long recipientId);
    NotificationResponse markAsRead(Long id, Long recipientId);
}
