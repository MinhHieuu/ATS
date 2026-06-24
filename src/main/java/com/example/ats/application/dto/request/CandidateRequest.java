package com.example.ats.application.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CandidateRequest extends UserRequest{
    @Size(max = 255, message = "LinkedIn URL must not exceed 255 characters")
    private String linkedinUrl;

    @Size(max = 255, message = "GitHub URL must not exceed 255 characters")
    private String githubUrl;

    @Size(max = 255, message = "Portfolio URL must not exceed 255 characters")
    private String portfolioUrl;

    @Size(max = 100, message = "Current position must not exceed 100 characters")
    private String currentPosition;

    @Min(value = 0, message = "Years of experience must be zero or greater")
    private Integer yearsOfExperience;
}
