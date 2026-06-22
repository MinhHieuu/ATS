package com.example.ats.domain.model;

import java.time.Instant;

public record User(
        Long id,
        String email,
        String fullname,
        String password,
        String phone,
        String avatarUrl,
        Boolean active,
        Instant createdAt,
        Instant updatedAt,
        Role role
) {

}
