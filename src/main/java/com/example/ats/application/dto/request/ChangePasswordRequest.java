package com.example.ats.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {
    @NotBlank(message = "old password is required")
    private String oldPassword;
    @NotBlank(message = "new password is required")
    private String newPassword;
    @NotBlank(message = "confirm password is required")
    private String confirmPassword;
}
