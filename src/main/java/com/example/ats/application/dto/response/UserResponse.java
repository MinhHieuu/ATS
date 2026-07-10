package com.example.ats.application.dto.response;

import com.example.ats.domain.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String email;
    private String fullname;
    private String phone;
    private String avatarUrl;
    private Role role;
}
