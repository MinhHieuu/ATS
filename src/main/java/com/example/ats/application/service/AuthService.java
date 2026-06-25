package com.example.ats.application.service;

import com.example.ats.application.dto.request.LoginRequest;
import com.example.ats.application.dto.response.LoginResponse;
import com.example.ats.application.mapper.UserMapper;
import com.example.ats.application.port.in.AuthUseCase;
import com.example.ats.application.port.out.RefreshTokenRepository;
import com.example.ats.application.port.out.UserRepository;
import com.example.ats.domain.exception.BusinessRuleException;
import com.example.ats.domain.exception.ResourceNotFoundException;
import com.example.ats.domain.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Transactional
public class AuthService implements AuthUseCase {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final UserMapper userMapper;

    public AuthService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository,
                       PasswordEncoder passwordEncoder, JWTService jwtService, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        String email = request.getEmail().trim();
        String password = request.getPassword().trim();
        if(email.isEmpty() || password.isEmpty()) {
            throw new BusinessRuleException("email or password not empty");
        }
        User user = userRepository.findByEmail(request.getEmail());
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResourceNotFoundException("email or password incorrect");
        }
        String accessToken = jwtService.generateAccessToken(user.getId(), user.getRole());
        String refreshToken = jwtService.generateRefreshToken(user.getId(), user.getRole());
        refreshTokenRepository.save(refreshToken, user.getId(), jwtService.getExpirationFromRefreshToken(refreshToken));
        return new LoginResponse(userMapper.toResponse(user), accessToken, refreshToken);
    }

    @Override
    public void logout(String refreshToken) {
        jwtService.getUserIdFromRefreshToken(refreshToken);
        if (!refreshTokenRepository.revoke(refreshToken)) {
            throw new BusinessRuleException("Invalid refresh token");
        }
    }

    @Override
    public String refreshToken(String refreshToken) {
        Long userId = jwtService.getUserIdFromRefreshToken(refreshToken);
        if (!refreshTokenRepository.isActive(refreshToken, Instant.now())) {
            throw new BusinessRuleException("Invalid refresh token");
        }
        User user = userRepository.findById(userId);
        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new BusinessRuleException("User is inactive");
        }
        return jwtService.generateAccessToken(user.getId(), user.getRole());
    }
}
