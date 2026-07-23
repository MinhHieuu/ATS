package com.example.ats.application.dto.request;

import com.example.ats.domain.model.InterviewResult;
import com.example.ats.domain.model.InterviewType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class InterviewRequest {
    @NotNull(message = "Application is required")
    private Long applicationId;

    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;

    @NotNull(message = "Scheduled time is required")
    private Instant scheduledAt;

    private Instant endAt;

    @Size(max = 255, message = "Location must not exceed 255 characters")
    private String location;

    private InterviewType type;

    @Size(max = 500, message = "Meeting link must not exceed 500 characters")
    private String meetingLink;

    private String notes;
}
