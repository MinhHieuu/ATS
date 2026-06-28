package com.example.ats.application.dto.request;

import com.example.ats.domain.model.InterviewStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InterviewStatusRequest {
    @NotNull(message = "Status is required")
    private InterviewStatus status;
}
