package com.example.ats.application.dto.response;

import com.example.ats.domain.model.ActivityAction;
import com.example.ats.domain.model.EntityType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityLogResponse {
    private Long id;
    private Long userId;
    private ActivityAction action;
    private EntityType entityType;
    private Long entityId;
    private String description;
    private Instant createdAt;
}
