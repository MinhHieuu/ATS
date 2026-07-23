package com.example.ats.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class Job {
    private Long id;
    private String title;
    private String description;
    private String requirements;
    private String location;
    private EmploymentType employmentType;
    private Long companyId;
    private Long categoryId;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private JobStatus status;
    private Long createdBy;
    private Instant createdAt;
    private Instant updatedAt;
}
