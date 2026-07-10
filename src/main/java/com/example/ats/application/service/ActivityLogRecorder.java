package com.example.ats.application.service;

import com.example.ats.application.port.out.ActivityLogRepository;
import com.example.ats.domain.model.ActivityAction;
import com.example.ats.domain.model.ActivityLog;
import com.example.ats.domain.model.EntityType;
import com.example.ats.domain.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class ActivityLogRecorder {
    private final ActivityLogRepository repository;

    public ActivityLogRecorder(ActivityLogRepository repository) {
        this.repository = repository;
    }

    public void record(ActivityAction action, EntityType entityType, Long entityId, String description) {
        repository.save(new ActivityLog(null, currentUserId(), action, entityType, entityId,
                description, Instant.now()));
    }

    private Long currentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User user)) {
            return null;
        }
        return user.getId();
    }
}
