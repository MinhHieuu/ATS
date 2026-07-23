package com.example.ats.infrastructure.persistence.entity;

import com.example.ats.domain.model.InterviewResult;
import com.example.ats.domain.model.InterviewType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "interviews")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InterviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "application_id", nullable = false)
    private ApplicationEntity application;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(name = "scheduled_at", nullable = false)
    private Instant scheduledAt;

    @Column(name = "end_at")
    private Instant endAt;

    @Column(length = 255)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private InterviewType type;

    @Column(name = "meeting_link", length = 500)
    private String meetingLink;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private InterviewResult result;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;
}
