package com.example.ats.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class Interview {
    private Long id;
    private Long jobApplicationId;
    private Long interviewerId;
    private String title;
    private Instant interviewTime;
    private String location;
    private InterviewStatus status;
    private Instant createdAt;

    public Interview changInterview(InterviewStatus status) {
        return new Interview(id, jobApplicationId, interviewerId, title, interviewTime, location, status, createdAt);
    }
}
