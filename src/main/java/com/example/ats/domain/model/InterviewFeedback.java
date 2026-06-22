package com.example.ats.domain.model;

import java.time.Instant;

public record InterviewFeedback(
    Long id,
    Long interviewId,
    Long recuiterId,
    Integer rating,
    String comment,
    String recommendation,
    Instant createdAt
) {
    
}
