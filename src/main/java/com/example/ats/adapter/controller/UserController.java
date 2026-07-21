package com.example.ats.adapter.controller;

import com.example.ats.application.dto.request.ChangePasswordRequest;
import com.example.ats.application.dto.request.UserRequest;
import com.example.ats.application.dto.response.UserResponse;
import com.example.ats.application.port.in.UserUseCase;
import com.example.ats.domain.model.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
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
        Number userId = ((Jwt) authentication.getPrincipal()).getClaim("userId");
        return ResponseEntity.ok(new ApiResponse("success", userUseCase.changePassword(userId.longValue(), request)));
    }

    @PatchMapping()
    public ResponseEntity<ApiResponse<UserResponse>> update(@RequestBody @Valid UserRequest request) {
        return ResponseEntity.ok(new ApiResponse("success", userUseCase.update(request)));
    }
}
