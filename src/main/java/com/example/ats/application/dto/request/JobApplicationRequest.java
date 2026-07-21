package com.example.ats.application.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

// Chi chua field do ung vien nhap. candidateId/status/appliedAt do server tu suy ra
// va duoc truyen qua tham so rieng cua use case, khong nhan tu request body.
@Getter
@Setter
public class JobApplicationRequest {
    @NotNull(message = "Job is required")
    private Long jobId;
    private Long resumeId;
    @Size(max = 100, message = "Source must not exceed 100 characters")
    private String source;
    @PositiveOrZero(message = "Expected salary must not be negative")
    private BigDecimal expectedSalary;
    private String note;
}
