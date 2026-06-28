package com.example.ats.application.dto.response;

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
    private Long recruiterId;
    private Integer rating;
    private String comment;
    private String recommendation;
    private Instant createdAt;
}
