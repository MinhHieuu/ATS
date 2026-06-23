package com.example.ats.application.dto.response;

public record RecruiterResponse(
        Long id,
        UserResponse user,
        Long companyId,
        String position
) {
}
