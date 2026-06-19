package com.example.ats.domain.model;

import java.math.BigDecimal;
import java.time.Instant;

public record JobApplication(
        Long id,
        Long candidateId,
        Long jobId,
        Long stageId,
        ApplicationStatus status,
        String source,
        BigDecimal expectedSalary,
        String note,
        Instant appliedAt,
        Instant updatedAt
) {
    public JobApplication changeStatus(ApplicationStatus newStatus, Instant changedAt) {
        return new JobApplication(
                id, candidateId, jobId, stageId, newStatus, source,
                expectedSalary, note, appliedAt, changedAt
        );
    }
}
