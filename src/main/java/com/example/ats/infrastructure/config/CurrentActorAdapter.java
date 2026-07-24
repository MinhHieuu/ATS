package com.example.ats.infrastructure.config;

import com.example.ats.application.port.out.CurrentActorPort;
import com.example.ats.domain.model.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class CurrentActorAdapter implements CurrentActorPort {

    @Override
    public Long currentUserId() {
        Jwt jwt = currentJwt();
        if (jwt == null) {
            return null;
        }
        Number userId = jwt.getClaim("userId");
        return userId != null ? userId.longValue() : null;
    }

    @Override
    public Role currentRole() {
        Jwt jwt = currentJwt();
        if (jwt == null) {
            return null;
        }
        String role = jwt.getClaimAsString("role");
        if (role == null || role.isBlank()) {
            return null;
        }
        try {
            return Role.valueOf(role);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private Jwt currentJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt;
        }
        return null;
    }
}
