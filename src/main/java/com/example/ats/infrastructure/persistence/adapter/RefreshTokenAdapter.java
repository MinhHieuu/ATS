package com.example.ats.infrastructure.persistence.adapter;

import com.example.ats.application.port.out.RefreshTokenRepository;
import com.example.ats.domain.exception.ResourceNotFoundException;
import com.example.ats.infrastructure.persistence.entity.RefreshTokenEntity;
import com.example.ats.infrastructure.persistence.entity.UserEntity;
import com.example.ats.infrastructure.persistence.repository.SpringDataRefreshTokenRepository;
import com.example.ats.infrastructure.persistence.repository.SpringDataUserRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public class RefreshTokenAdapter implements RefreshTokenRepository {
    private final SpringDataRefreshTokenRepository refreshTokenRepository;
    private final SpringDataUserRepository userRepository;

    public RefreshTokenAdapter(SpringDataRefreshTokenRepository refreshTokenRepository,
                               SpringDataUserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void save(String token, Long userId, Instant expiresAt) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        refreshTokenRepository.save(RefreshTokenEntity.builder()
                .token(token)
                .user(user)
                .revoked(false)
                .createdAt(Instant.now())
                .expiresAt(expiresAt)
                .build());
    }

    @Override
    public boolean isActive(String token, Instant now) {
        return refreshTokenRepository.findByToken(token)
                .filter(refreshToken -> !Boolean.TRUE.equals(refreshToken.getRevoked()))
                .filter(refreshToken -> refreshToken.getExpiresAt().isAfter(now))
                .isPresent();
    }

    @Override
    public boolean revoke(String token) {
        return refreshTokenRepository.findByToken(token)
                .map(refreshToken -> {
                    refreshToken.setRevoked(true);
                    refreshTokenRepository.save(refreshToken);
                    return true;
                })
                .orElse(false);
    }
}
