package com.example.ats.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class Candidate {
    private Long id;
    private Long userId;
    private String linkedinUrl;
    private String githubUrl;
    private String portfolioUrl;
    private String currentPosition;
    private Integer yearsOfExperience;
    private Instant createdAt;
    private Instant updatedAt;
}
