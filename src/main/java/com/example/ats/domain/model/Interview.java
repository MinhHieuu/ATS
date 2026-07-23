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
    private Long applicationId;
    private String title;
    private Instant scheduledAt;
    private Instant endAt;
    private String location;
    private InterviewType type;
    private String meetingLink;
    private String notes;
    private InterviewResult result;
    private Instant createdAt;
    private Instant updatedAt;
}
