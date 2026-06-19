package com.example.ats.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "interview_feedbacks")
@Getter
@Setter
@NoArgsConstructor
public class InterviewFeedbackEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "interview_id", nullable = false)
    private InterviewEntity interview;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reviewer_id", nullable = false)
    private UserEntity reviewer;
    private Integer rating;
    @Column(columnDefinition = "TEXT")
    private String comment;
    @Column(length = 50)
    private String recommendation;
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
