package com.example.ats.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "candidates")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CandidateEntity {
    @Id
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    @MapsId
    private UserEntity user;
    @Column(name = "linkedin_url", length = 255)
    private String linkedinUrl;
    @Column(name = "github_url", length = 255)
    private String githubUrl;
    @Column(name = "portfolio_url", length = 255)
    private String portfolioUrl;
    @Column(name = "current_position", length = 100)
    private String currentPosition;
    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
    @Column(name = "updated_at")
    private Instant updatedAt;
    @OneToMany(mappedBy = "candidate")
    private List<ResumeEntity> resumes;
    @OneToMany(mappedBy = "candidate")
    private List<ApplicationEntity> applications;

    public CandidateEntity(Long id, UserEntity user, String linkedinUrl, String githubUrl, String portfolioUrl,
                           String currentPosition, Integer yearsOfExperience, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.user = user;
        this.linkedinUrl = linkedinUrl;
        this.githubUrl = githubUrl;
        this.portfolioUrl = portfolioUrl;
        this.currentPosition = currentPosition;
        this.yearsOfExperience = yearsOfExperience;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
