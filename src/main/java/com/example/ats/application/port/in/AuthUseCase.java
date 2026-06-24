package com.example.ats.application.port.in;

import com.example.ats.application.dto.request.LoginRequest;
import com.example.ats.application.dto.response.LoginResponse;

public interface AuthUseCase {
    LoginResponse login(LoginRequest request);
    Void logout();
    String refreshToken();
}
