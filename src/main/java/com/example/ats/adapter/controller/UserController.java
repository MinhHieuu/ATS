package com.example.ats.adapter.controller;

import com.example.ats.application.dto.request.ChangePasswordRequest;
import com.example.ats.application.port.in.UserUseCase;
import com.example.ats.domain.model.ApiResponse;
import com.example.ats.domain.model.User;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserUseCase userUseCase;
    public UserController(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    @PatchMapping("password")
    public ResponseEntity<ApiResponse<Void>> changePassword(Authentication authentication,
                                                           @Valid @RequestBody ChangePasswordRequest request) {
        Long userId = ((User) authentication.getPrincipal()).getId();
        return ResponseEntity.ok(new ApiResponse("success", userUseCase.changePassword(userId, request)));
    }
}
