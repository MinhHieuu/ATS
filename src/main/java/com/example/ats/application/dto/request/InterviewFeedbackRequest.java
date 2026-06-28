package com.example.ats.application.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InterviewFeedbackRequest {
    @NotNull(message = "Interview id is required")
    private Long interviewId;
    @NotNull(message = "Recruiter id is required")
    private Long recruiterId;
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;
    private String comment;
    @Size(max = 50, message = "Recommendation must not exceed 50 characters")
    private String recommendation;
}
