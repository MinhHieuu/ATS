package com.example.ats.domain.model;

import java.math.BigDecimal;
import java.time.Instant;

public record Job(
        Long id,
        String title,
        String description,
        String requirements,
        String location,
        String employmentType,
        BigDecimal salaryMin,
        BigDecimal salaryMax,
        JobStatus status,
        Long createdBy,
        Instant createdAt,
        Instant updatedAt
) {
}
