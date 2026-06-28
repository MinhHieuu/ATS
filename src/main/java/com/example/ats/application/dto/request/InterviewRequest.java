package com.example.ats.application.dto.request;

import com.example.ats.domain.model.InterviewStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class InterviewRequest {
    @NotNull(message = "Application id is required")
    private Long jobApplicationId;
    private Long interviewerId;
    @Size(max = 150, message = "Title must not exceed 150 characters")
    private String title;
    @NotNull(message = "Interview time is required")
    private Instant interviewTime;
    private String location;
    private InterviewStatus status;
}
