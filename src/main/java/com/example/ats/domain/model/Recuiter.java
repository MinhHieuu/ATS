package com.example.ats.domain.model;

public record Recuiter(
    Long id,
    Long userId,
    Long companyId,
    String position
) {
    
}
