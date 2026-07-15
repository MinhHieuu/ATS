package com.example.ats.infrastructure.persistence.entity;

import com.example.ats.domain.model.ApplicationStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "applications")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApplicationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "candidate_id", nullable = false)
    private CandidateEntity candidate;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_id", nullable = false)
    private JobEntity job;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ApplicationStatus status;
    @Column(length = 100)
    private String source;
    @Column(name = "expected_salary", precision = 12, scale = 2)
    private BigDecimal expectedSalary;
    @Column(columnDefinition = "TEXT")
    private String note;
    @Column(name = "applied_at", nullable = false)
    private Instant appliedAt;
    @Column(name = "updated_at")
    private Instant updatedAt;
}
