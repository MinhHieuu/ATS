package com.example.ats.application.service;

import com.example.ats.application.dto.request.LoginRequest;
import com.example.ats.application.dto.response.LoginResponse;
import com.example.ats.application.mapper.UserMapper;
import com.example.ats.application.port.in.AuthUseCase;
import com.example.ats.application.port.out.UserRepository;
import com.example.ats.domain.exception.BusinessRuleException;
import com.example.ats.domain.exception.ResourceNotFoundException;
import com.example.ats.domain.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements AuthUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final UserMapper userMapper;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JWTService jwtService, UserMapper userMapper) {
        this.userRepository = userRepository;
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
        return new LoginResponse(userMapper.toResponse(user), accessToken, refreshToken);
    }

    @Override
    public Void logout() {
        return null;
    }

    @Override
    public String refreshToken() {
        return "";
    }
}
