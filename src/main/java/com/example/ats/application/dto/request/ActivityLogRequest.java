package com.example.ats.application.dto.request;

import com.example.ats.domain.model.ActivityAction;
import com.example.ats.domain.model.EntityType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivityLogRequest {
    private Long userId;
    @NotNull(message = "Action is required")
    private ActivityAction action;
    @NotNull(message = "Entity type is required")
    private EntityType entityType;
    private Long entityId;
    private String description;
}
