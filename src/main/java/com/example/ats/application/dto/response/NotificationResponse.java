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
    private NotificationType type;
    private String title;
    private String content;
    private Long referenceId;
    private Boolean read;
    private Instant createdAt;
}
