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
    private Long recruiterId;
    private Integer rating;
    private String comment;
    private String recommendation;
    private Instant createdAt;
}
