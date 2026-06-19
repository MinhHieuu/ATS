package com.example.ats.domain.model;

import java.time.Instant;

public record Interview(
    Long id,
    Long jobApplicationId,
    Long interviewerId,
    String title,
    Instant interviewTime,
    String location,
    InterviewStatus status,
    Instant createdAt
) {
    public Interview changInterview(InterviewStatus status) {
        return new Interview(id, jobApplicationId, interviewerId, title, interviewTime, location, status, createdAt);
    }
}
