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
    private static final String ACCESS_TOKEN_TYPE = "ACCESS";
    private static final String REFRESH_TOKEN_TYPE = "REFRESH";

    @Value("${jwt.secret-key}")
    private String secret;
    @Value("${jwt.access-expiration}")
    private long accessExpiration;
    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    private String generateToken(Long userId, Role role, Long expiration, String tokenType) {
        Map<String, Object> claims = Map.of(
                "userId", userId,
                "role", role,
                "type", tokenType,
                "jti", UUID.randomUUID().toString()
        );
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
        return generateToken(userId, role, refreshExpiration, REFRESH_TOKEN_TYPE);
    }

    public String generateAccessToken(Long userId, Role role) {
        return generateToken(userId, role, accessExpiration, ACCESS_TOKEN_TYPE);
    }

    public Long getUserIdFromRefreshToken(String token) {
        try {
            return getUserId(token, REFRESH_TOKEN_TYPE);
        } catch (JwtException | IllegalArgumentException e) {
            throw new BusinessRuleException("Invalid refresh token");
        }
    }

    public Instant getExpirationFromRefreshToken(String token) {
        try {
            if (token == null || token.isBlank()) {
                throw new IllegalArgumentException("Invalid refresh token");
            }
            return getValidatedClaims(token, REFRESH_TOKEN_TYPE).getExpiration().toInstant();
        } catch (JwtException | IllegalArgumentException e) {
            throw new BusinessRuleException("Invalid refresh token");
        }
    }

    public String refreshAccessToken(String token, Role role) {
        Long userId = getUserIdFromRefreshToken(token);
        return generateToken(userId, role, accessExpiration, ACCESS_TOKEN_TYPE);
    }

    public Long getUserIdFromToken(String token) {
        return getUserId(token, ACCESS_TOKEN_TYPE);
    }

    private Long getUserId(String token, String expectedTokenType) {
        Claims claims = getValidatedClaims(token, expectedTokenType);
        Number userId = claims.get("userId", Number.class);
        if (userId == null) {
            throw new IllegalArgumentException("Invalid token");
        }
        return userId.longValue();
    }

    public Role getRoleFromToken(String token) {
        Claims claims = getValidatedClaims(token, ACCESS_TOKEN_TYPE);
        String role = claims.get("role", String.class);
        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException("Invalid access token");
        }
        return Role.valueOf(role);
    }

    public boolean isTokenValid(String token) {
        try {
            getValidatedClaims(token, ACCESS_TOKEN_TYPE);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims getValidatedClaims(String token, String expectedTokenType) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Invalid token");
        }
        Claims claims = getClaims(token);
        String tokenType = claims.get("type", String.class);
        if (!expectedTokenType.equals(tokenType)) {
            throw new IllegalArgumentException("Invalid token type");
        }
        return claims;
    }
}
