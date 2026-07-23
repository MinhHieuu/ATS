package com.example.ats.application.dto.response;

import com.example.ats.domain.model.InterviewResult;
import com.example.ats.domain.model.InterviewType;
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
    private Long applicationId;
    private String candidateName;
    private String jobTitle;
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
