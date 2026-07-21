package com.example.ats.adapter.controller;

import com.example.ats.application.dto.response.NotificationResponse;
import com.example.ats.application.port.in.NotificationUseCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationUseCase notificationUseCase;

    public NotificationController(NotificationUseCase notificationUseCase) {
        this.notificationUseCase = notificationUseCase;
    }

    @GetMapping
    public ResponseEntity<Page<NotificationResponse>> getMyNotifications(Authentication authentication,
                                                                          Pageable pageable) {
        long userId = getUserId(authentication);
        return ResponseEntity.ok(notificationUseCase.findByRecipient(userId, pageable));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(Authentication authentication) {
        long userId = getUserId(authentication);
        return ResponseEntity.ok(Map.of("count", notificationUseCase.countUnread(userId)));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<NotificationResponse> markAsRead(@PathVariable Long id,
                                                            Authentication authentication) {
        long userId = getUserId(authentication);
        return ResponseEntity.ok(notificationUseCase.markAsRead(id, userId));
    }

    @PatchMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead(Authentication authentication) {
        long userId = getUserId(authentication);
        notificationUseCase.markAllAsRead(userId);
        return ResponseEntity.noContent().build();
    }

    private long getUserId(Authentication authentication) {
        return ((Number) ((Jwt) authentication.getPrincipal()).getClaim("userId")).longValue();
    }
}
