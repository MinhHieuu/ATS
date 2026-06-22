package com.example.ats.domain.model;

import java.time.Instant;

public record Candidate(
        Long id,
        Long userId,
        String linkedinUrl,
        String githubUrl,
        String portfolioUrl,
        String currentPosition,
        Integer yearsOfExperience,
        Instant createdAt,
        Instant updatedAt
) {
}
