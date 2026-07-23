package com.example.ats.application.dto.request;

import com.example.ats.domain.model.InterviewResult;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InterviewResultRequest {
    @NotNull(message = "Result is required")
    private InterviewResult result;
}
