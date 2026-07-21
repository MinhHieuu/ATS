package com.example.ats.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class JobApplication {
    private Long id;
    private Long candidateId;
    private Long jobId;
    private Long resumeId;
    private ApplicationStatus status;
    private String source;
    private BigDecimal expectedSalary;
    private String note;
    private Instant appliedAt;
    private Instant updatedAt;
}
