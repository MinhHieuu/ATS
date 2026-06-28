package com.example.ats.application.dto.request;

import com.example.ats.domain.model.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class JobApplicationRequest {
    @NotNull(message = "Candidate id is required")
    private Long candidateId;

    @NotNull(message = "Job id is required")
    private Long jobId;

    private Long stageId;
    private ApplicationStatus status;

    @Size(max = 100, message = "Source must not exceed 100 characters")
    private String source;

    @PositiveOrZero(message = "Expected salary must be greater than or equal to 0")
    private BigDecimal expectedSalary;

    private String note;
}
