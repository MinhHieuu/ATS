package com.example.ats.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "interviews")
@Getter
@Setter
@NoArgsConstructor
public class InterviewEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "application_id", nullable = false)
    private ApplicationEntity application;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interviewer_id")
    private UserEntity interviewer;
    @Column(length = 150)
    private String title;
    @Column(name = "interview_time", nullable = false)
    private Instant interviewTime;
    @Column(name = "meeting_link")
    private String meetingLink;
    private String location;
    @Column(length = 50)
    private String status;
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
    @OneToMany(mappedBy = "interview")
    private List<InterviewFeedbackEntity> feedbacks;
}
