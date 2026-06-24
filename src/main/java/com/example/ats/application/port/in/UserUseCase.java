package com.example.ats.application.port.in;

import com.example.ats.application.dto.request.ChangePasswordRequest;
import com.example.ats.application.dto.request.UserRequest;
import com.example.ats.application.dto.response.UserResponse;
import com.example.ats.domain.model.Role;


public interface UserUseCase {
    UserResponse create(UserRequest request, Role role);
    UserResponse findById(Long id);
    UserResponse update(UserRequest request);
    Void changePassword(Long id, ChangePasswordRequest request);
}
