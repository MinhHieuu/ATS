package com.example.ats.application.dto.response;

import com.example.ats.domain.model.FeedbackRecommendation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewFeedbackResponse {
    private Long id;
    private Long interviewId;
    private String interviewTitle;
    private Long reviewerId;
    private String reviewerName;
    private String candidateName;
    private String jobTitle;
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
