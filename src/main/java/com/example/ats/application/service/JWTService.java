package com.example.ats.application.service;

import com.example.ats.domain.model.Role;
import io.jsonwebtoken.Claims;
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

@Service
public class JWTService  {
    @Value("${jwt.secret-key}")
    private String secret;
    @Value("${jwt.access-expiration}")
    private long accessExpiration;
    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    private String generateToken(Long userId, Role role, Long expiration) {
        Map<String, Object> claims = Map.of("userId", userId, "role", role);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(new Date(expiration))
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
}
