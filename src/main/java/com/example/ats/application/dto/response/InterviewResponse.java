package com.example.ats.application.dto.response;

import com.example.ats.domain.model.InterviewStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewResponse {
    private Long id;
    private Long jobApplicationId;
    private Long interviewerId;
    private String title;
    private Instant interviewTime;
    private String location;
    private InterviewStatus status;
    private Instant createdAt;
}
