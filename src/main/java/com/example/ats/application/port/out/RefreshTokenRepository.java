package com.example.ats.application.port.out;

import java.time.Instant;

public interface RefreshTokenRepository {
    void save(String token, Long userId, Instant expiresAt);
    boolean isActive(String token, Instant now);
    boolean revoke(String token);
}
