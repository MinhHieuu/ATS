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
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private CompanyEntity company;
    // Nullable de cac job da ton tai truoc khi co bang categories khong bi vo du lieu.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;
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

    public JobEntity(Long id, String title, String description, String requirements, String location,
                     EmploymentType employmentType, CompanyEntity company, CategoryEntity category,
                     BigDecimal salaryMin, BigDecimal salaryMax,
                     JobStatus status, UserEntity createdBy, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.requirements = requirements;
        this.location = location;
        this.employmentType = employmentType;
        this.company = company;
        this.category = category;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.status = status;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
