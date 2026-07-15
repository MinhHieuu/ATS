package com.example.ats.application.dto.request;

import com.example.ats.domain.model.EmploymentType;
import com.example.ats.domain.model.JobStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class JobRequest {
    @NotBlank(message = "Title is required")
    @Size(max = 150, message = "Title must not exceed 150 characters")
    private String title;
    private String description;
    private String requirements;
    @Size(max = 100, message = "Location must not exceed 100 characters")
    private String location;
    private EmploymentType employmentType;
    @NotNull(message = "Company is required")
    private Long companyId;
    @PositiveOrZero(message = "Minimum salary must be greater than or equal to 0")
    private BigDecimal salaryMin;
    @PositiveOrZero(message = "Maximum salary must be greater than or equal to 0")
    private BigDecimal salaryMax;
    private JobStatus status;
    private Long createdBy;
}
