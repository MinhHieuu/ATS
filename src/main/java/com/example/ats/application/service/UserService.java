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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        String email = trimToEmpty(request.getEmail());
        String password = trimToEmpty(request.getPassword());
        String phone = trimToEmpty(request.getPhone());
        if (password.isEmpty()) {
            password = "password123";
        }
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
        return mapper.toResponse(userRepository.save(new User(null, email, request.getFullname(), encode.encode(password),
         request.getPhone(), "default-avatar.jpg", true, now, null, role)));
    }

    @Override
    public UserResponse findById(Long id) {
        return mapper.toResponse(userRepository.findById(id));
    }

    @Override
    public Page<UserResponse> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    public Page<UserResponse> searchByFullnameOrEmailAndRole(String keyword, Role role, Pageable pageable) {
        // Keyword rong -> like '%%' -> tra ve tat ca, dung voi hanh vi o tim kiem khi chua go gi.
        return userRepository.searchByFullnameOrEmailAndRole(trimToEmpty(keyword), role, pageable)
                .map(mapper::toResponse);
    }

    @Override
    public UserResponse update(UserRequest request) {
        String email = trimToEmpty(request.getEmail());
        User user = userRepository.findByEmail(email);
        return updateUser(request, user);
    }

    @Override
    public UserResponse update(Long id, UserRequest request) {
        User user = userRepository.findById(id);
        return updateUser(request, user);
    }

    @Override
    public UserResponse activate(Long id) {
        return updateActive(id, true);
    }

    @Override
    public UserResponse deactivate(Long id, Long currentUserId) {
        // Chan tu vo hieu hoa: AuthService khong cho user inactive dang nhap lai,
        // nen admin tu deactivate se tu khoa minh vinh vien, khong cuu duoc qua API.
        if (id.equals(currentUserId)) {
            throw new BusinessRuleException("Cannot deactivate your own account");
        }
        return updateActive(id, false);
    }

    private UserResponse updateActive(Long id, boolean active) {
        User user = userRepository.findById(id);
        user.setActive(active);
        user.setUpdatedAt(Instant.now());
        return mapper.toResponse(userRepository.save(user));
    }

    private UserResponse updateUser(UserRequest request, User user) {
        user.setFullname(trimToEmpty(request.getFullname()));
        user.setPhone(trimToEmpty(request.getPhone()));
        if(request.getAvatar() != null && !request.getAvatar().isEmpty()) {
            user.setAvatarUrl(request.getAvatar());
        }
        Instant now = Instant.now();
        user.setUpdatedAt(now);
        return mapper.toResponse(userRepository.save(user));
    }

    @Override
    public Void changePassword(Long id, ChangePasswordRequest request) {
        User user = userRepository.findById(id);
        String oldPassword = trimToEmpty(request.getOldPassword());
        String newPassword = trimToEmpty(request.getNewPassword());
        String confirmPassword = trimToEmpty(request.getConfirmPassword());
        if(!encode.matches(oldPassword, user.getPassword())) {
            throw new BusinessRuleException("Old password does not match");
        }
        if(encode.matches(newPassword, user.getPassword())) {
            throw new BusinessRuleException("New password must be different from old password");
        }
        if(!newPassword.equals(confirmPassword)) {
            throw new BusinessRuleException("Confirm password does not match");
        }
        user.setPassword(encode.encode(newPassword));
        userRepository.save(user);
        return null;
    }

    @Override
    public UserResponse findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return mapper.toResponse(user);
    }

    private String trimToEmpty(String value) {
        return value == null ? "" : value.trim();
    }
}
