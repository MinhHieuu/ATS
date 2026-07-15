package com.example.ats.application.dto.request;

import com.example.ats.domain.model.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationStatusRequest {
    @NotNull(message = "Status is required")
    private ApplicationStatus status;
}
