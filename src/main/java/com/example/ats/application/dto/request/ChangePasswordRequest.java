package com.example.ats.application.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {
    @JsonAlias({"oldpassword", "old_password", "currentPassword", "currentpassword", "current_password"})
    @NotBlank(message = "old password is required")
    private String oldPassword;
    @JsonAlias({"newpassword", "new_password"})
    @NotBlank(message = "new password is required")
    private String newPassword;
    @JsonAlias({"confirmpassword", "confirm_password"})
    @NotBlank(message = "confirm password is required")
    private String confirmPassword;
}
