package com.example.ats.application.dto.response;

import com.example.ats.domain.model.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobApplicationResponse {
    private Long id;
    private Long candidateId;
    private Long jobId;
    private Long stageId;
    private ApplicationStatus status;
    private String source;
    private BigDecimal expectedSalary;
    private String note;
    private Instant appliedAt;
    private Instant updatedAt;
}
