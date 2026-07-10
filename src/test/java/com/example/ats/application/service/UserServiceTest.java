package com.example.ats.application.service;

import com.example.ats.application.dto.request.ChangePasswordRequest;
import com.example.ats.application.mapper.UserMapper;
import com.example.ats.application.port.out.UserRepository;
import com.example.ats.domain.model.Role;
import com.example.ats.domain.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Test
    void changePasswordAcceptsLowercaseOldPasswordFieldAndUpdatesHash() throws Exception {
        ChangePasswordRequest request = objectMapper.readValue("""
                {
                  "oldpassword": "password123",
                  "newPassword": "newPassword123",
                  "confirmPassword": "newPassword123"
                }
                """, ChangePasswordRequest.class);
        User user = new User(1L, "candidate@example.com", "Lan Nguyen",
                passwordEncoder.encode("password123"), "0901234567", "default-avatar.png",
                true, Instant.now(), null, Role.CANDIDATE);

        when(userRepository.findById(1L)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserService userService = new UserService(userRepository, passwordEncoder, userMapper);

        assertDoesNotThrow(() -> userService.changePassword(1L, request));

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        assertThat(passwordEncoder.matches("newPassword123", userCaptor.getValue().getPassword())).isTrue();
    }
}
