package com.example.ats.application.service;

import com.example.ats.application.dto.request.ChangePasswordRequest;
import com.example.ats.application.dto.request.UserRequest;

import com.example.ats.application.dto.response.UserResponse;
import com.example.ats.application.mapper.UserMapper;
import com.example.ats.application.port.in.UserUseCase;
import com.example.ats.application.port.out.UserRepository;
import com.example.ats.domain.exception.BusinessRuleException;
import com.example.ats.domain.model.Role;
import com.example.ats.domain.model.User;
import jakarta.transaction.Transactional;

import java.time.Instant;
import java.util.regex.Pattern;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class UserService implements UserUseCase {
    private final UserRepository userRepository;
    private final PasswordEncoder encode;
    private final UserMapper mapper;
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z][A-Za-z0-9._%+-]*@[A-Za-z0-9-]+(?:\\.[A-Za-z0-9-]+)+$");
    public UserService(UserRepository userRepository, PasswordEncoder encode, UserMapper mapper) {
        this.userRepository = userRepository;
        this.encode = encode;
        this.mapper = mapper;
    }

    @Override
    public UserResponse create(UserRequest request, Role role) {
        String email = request.getEmail().trim();
        String password = request.getPassword().trim();
        String phone = request.getPhone().trim();
        if(email.isEmpty() || password.isEmpty()) {
            throw new BusinessRuleException("Email and password cannot be empty");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new BusinessRuleException(
                    "Email must start with a letter and contain a valid domain");
        }
        if(userRepository.existsByEmail(email)) {
            throw new BusinessRuleException("Email already exists");
        }
        if(userRepository.existsByPhone(phone)) {
            throw new BusinessRuleException("Phone already exists");
        }
        Instant now = Instant.now();
        return mapper.toResponse(userRepository.save(new User(null, email, request.getFullName(), encode.encode(password),
         request.getPhone(), "default-avatar.png", true, now, null, role)));
    }

    @Override
    public UserResponse findById(Long id) {
        return mapper.toResponse(userRepository.findById(id));
    }

    @Override
    public UserResponse update(UserRequest request) {
        String email = request.getEmail().trim();
        User user = userRepository.findByEmail(email);
        user.setFullname(request.getFullName().trim());
        user.setPhone(request.getPhone().trim());
        if(!request.getAvatar().isEmpty()) {
            user.setAvatarUrl(request.getAvatar());
        }
        Instant now = Instant.now();
        user.setUpdatedAt(now);
        return mapper.toResponse(userRepository.save(user));
    }

    @Override
    public Void changePassword(Long id, ChangePasswordRequest request) {
        User user = userRepository.findById(id);
        if(!encode.matches(request.getOldPassword(), user.getPassword())) {
            throw new BusinessRuleException("Old password does not match");
        }
        if(encode.matches(request.getNewPassword(), user.getPassword())) {
            throw new BusinessRuleException("New password must be different from old password");
        }
        if(!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BusinessRuleException("Confirm password does not match");
        }
        user.setPassword(encode.encode(request.getNewPassword()));
        userRepository.save(user);
        return null;
    }

    @Override
    public UserResponse findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return mapper.toResponse(user);
    }

}
