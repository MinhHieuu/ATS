package com.example.ats.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "resumes")
@Getter
@Setter
@NoArgsConstructor
public class ResumeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "candidate_id", nullable = false)
    private CandidateEntity candidate;
    @Column(name = "file_name", nullable = false)
    private String fileName;
    @Column(name = "file_url", nullable = false)
    private String fileUrl;
    @Column(name = "uploaded_at", nullable = false)
    private Instant uploadedAt;

}
