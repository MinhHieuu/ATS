package com.example.ats.application.dto.response;

import com.example.ats.domain.model.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private Long id;
    private Long recipientId;
    private NotificationType type;
    private String title;
    private String message;
    private Long relatedApplicationId;
    private Long relatedJobId;
    private Boolean read;
    private Instant createdAt;
}
