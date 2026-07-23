package com.example.ats.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class InterviewFeedback {
    private Long id;
    private Long interviewId;
    private Long reviewerId;
    private Integer overallRating;
    private Integer technicalRating;
    private Integer communicationRating;
    private String strengths;
    private String weaknesses;
    private String comments;
    private FeedbackRecommendation recommendation;
    private Instant createdAt;
    private Instant updatedAt;
}
