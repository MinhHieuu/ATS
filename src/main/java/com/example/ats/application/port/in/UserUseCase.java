package com.example.ats.application.port.in;

import com.example.ats.application.dto.request.ChangePasswordRequest;
import com.example.ats.application.dto.request.UserRequest;
import com.example.ats.application.dto.response.UserResponse;
import com.example.ats.domain.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface UserUseCase {
    UserResponse create(UserRequest request, Role role);
    UserResponse findById(Long id);
    Page<UserResponse> findAll(Pageable pageable);
    Page<UserResponse> searchByFullnameOrEmailAndRole(String keyword, Role role, Pageable pageable);
    UserResponse update(UserRequest request);
    UserResponse update(Long id, UserRequest request);
    UserResponse activate(Long id);
    UserResponse deactivate(Long id, Long currentUserId);
    Void changePassword(Long id, ChangePasswordRequest request);
    UserResponse findByEmail(String email);
}
