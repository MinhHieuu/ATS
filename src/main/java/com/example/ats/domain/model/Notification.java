package com.example.ats.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class Notification {
    private Long id;
    private Long recipientId;
    private NotificationType type;
    private String title;
    private String content;
    private Long referenceId;
    private Boolean read;
    private Instant createdAt;
}
