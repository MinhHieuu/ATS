package com.example.ats.domain.model;

import java.time.Instant;

public record Resume(
    Long id,
    Long candidateId,
    String fileName,
    String fileUrl,
    Instant createdAt
) {
    
}
