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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
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
}
