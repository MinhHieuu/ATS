package com.example.ats.application.dto.request;

import com.example.ats.domain.model.FeedbackRecommendation;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InterviewFeedbackRequest {
    @NotNull(message = "Interview is required")
    private Long interviewId;

    @NotNull(message = "Overall rating is required")
    @Min(value = 1, message = "Overall rating must be between 1 and 5")
    @Max(value = 5, message = "Overall rating must be between 1 and 5")
    private Integer overallRating;

    @Min(value = 1, message = "Technical rating must be between 1 and 5")
    @Max(value = 5, message = "Technical rating must be between 1 and 5")
    private Integer technicalRating;

    @Min(value = 1, message = "Communication rating must be between 1 and 5")
    @Max(value = 5, message = "Communication rating must be between 1 and 5")
    private Integer communicationRating;

    @Size(max = 2000, message = "Strengths must not exceed 2000 characters")
    private String strengths;

    @Size(max = 2000, message = "Weaknesses must not exceed 2000 characters")
    private String weaknesses;

    @Size(max = 2000, message = "Comments must not exceed 2000 characters")
    private String comments;

    @NotNull(message = "Recommendation is required")
    private FeedbackRecommendation recommendation;
}
