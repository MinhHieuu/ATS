package com.example.ats.application.service;

import com.example.ats.domain.exception.BusinessRuleException;
import com.example.ats.domain.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
public class JWTService {
    @Value("${jwt.secret-key}")
    private String secret;
    @Value("${jwt.access-expiration}")
    private long accessExpiration;
    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    private String generateToken(Long userId, Role role, Long expiration) {
        Map<String, Object> claims = Map.of("userId", userId, "role", role, "jti", UUID.randomUUID().toString());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(expiration)))
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build().parseClaimsJws(token)
                .getBody();
    }

    private Key getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateRefreshToken(Long userId, Role role) {
        return generateToken(userId, role, refreshExpiration);
    }

    public String generateAccessToken(Long userId, Role role) {
        return generateToken(userId, role, accessExpiration);
    }

    public Long getUserIdFromRefreshToken(String token) {
        try {
            if (token == null || token.isBlank()) {
                throw new IllegalArgumentException("Invalid refresh token");
            }
            Claims claims = getClaims(token);
            Number userId = claims.get("userId", Number.class);
            if (userId == null) {
                throw new IllegalArgumentException("Invalid refresh token");
            }
            return userId.longValue();
        } catch (JwtException | IllegalArgumentException e) {
            throw new BusinessRuleException("Invalid refresh token");
        }
    }

    public Instant getExpirationFromRefreshToken(String token) {
        try {
            if (token == null || token.isBlank()) {
                throw new IllegalArgumentException("Invalid refresh token");
            }
            return getClaims(token).getExpiration().toInstant();
        } catch (JwtException | IllegalArgumentException e) {
            throw new BusinessRuleException("Invalid refresh token");
        }
    }

    public String refreshAccessToken(String token, Role role) {
        Long userId = getUserIdFromRefreshToken(token);
        return generateToken(userId, role, accessExpiration);
    }
}
