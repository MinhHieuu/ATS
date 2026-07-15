package com.example.ats.infrastructure.persistence.entity;

import com.example.ats.domain.model.EmploymentType;
import com.example.ats.domain.model.JobStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "jobs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 150)
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(columnDefinition = "TEXT")
    private String requirements;
    @Column(length = 100)
    private String location;
    @Column(name = "employment_type")
    @Enumerated(EnumType.STRING)
    private EmploymentType employmentType;
    @Column(name = "salary_min", precision = 12, scale = 2)
    private BigDecimal salaryMin;
    @Column(name = "salary_max", precision = 12, scale = 2)
    private BigDecimal salaryMax;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private JobStatus status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private UserEntity createdBy;
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
    @Column(name = "updated_at")
    private Instant updatedAt;
    @OneToMany(mappedBy = "job")
    private List<ApplicationEntity> applications;

}
