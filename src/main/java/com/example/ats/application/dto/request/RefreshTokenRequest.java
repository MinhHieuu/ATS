package com.example.ats.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class RefreshTokenRequest {
    @NotBlank(message = "refreshToken is required")
    private String refreshToken;
}
