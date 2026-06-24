package com.example.ats.adapter.controller;

import com.example.ats.application.dto.request.CandidateRequest;
import com.example.ats.application.dto.response.CandidateResponse;
import com.example.ats.application.port.in.CandidateUseCase;
import com.example.ats.domain.model.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final CandidateUseCase candidateUseCase;

    public AuthController(CandidateUseCase candidateUseCase) {
        this.candidateUseCase = candidateUseCase;
    }

     @PostMapping("register")
     public ResponseEntity<ApiResponse<CandidateResponse>> register(@Valid @RequestBody CandidateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("create success", candidateUseCase.create(request)));
        
     }
}
