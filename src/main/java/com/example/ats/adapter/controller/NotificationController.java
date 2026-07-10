package com.example.ats.adapter.controller;

import com.example.ats.application.dto.response.NotificationResponse;
import com.example.ats.application.port.in.NotificationUseCase;
import com.example.ats.domain.model.ApiResponse;
import com.example.ats.domain.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationUseCase useCase;

    public NotificationController(NotificationUseCase useCase) {
        this.useCase = useCase;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> findMine(Authentication authentication) {
        Long userId = ((User) authentication.getPrincipal()).getId();
        return ResponseEntity.ok(new ApiResponse<>("success", useCase.findByRecipient(userId)));
    }

    @PatchMapping("{id}/read")
    public ResponseEntity<ApiResponse<NotificationResponse>> markAsRead(@PathVariable Long id,
                                                                        Authentication authentication) {
        Long userId = ((User) authentication.getPrincipal()).getId();
        return ResponseEntity.ok(new ApiResponse<>("success", useCase.markAsRead(id, userId)));
    }
}
