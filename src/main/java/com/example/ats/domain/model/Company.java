package com.example.ats.domain.model;

import java.time.Instant;

public record Company(
        Long id,
        String name,
        String logo,
        String email,
        String website,
        String description,
        String address,
        Instant createdAt,
        Instant updatedAt
) {

}
