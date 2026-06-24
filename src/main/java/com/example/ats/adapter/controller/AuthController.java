package com.example.ats.adapter.controller;

import com.example.ats.application.dto.request.CandidateRequest;
import com.example.ats.application.dto.request.RecruiterRequest;
import com.example.ats.application.dto.response.CandidateResponse;
import com.example.ats.application.dto.response.RecruiterResponse;
import com.example.ats.application.port.in.CandidateUseCase;
import com.example.ats.application.port.in.RecruiterUseCase;
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
    private final RecruiterUseCase recruiterUseCase;

    public AuthController(CandidateUseCase candidateUseCase, RecruiterUseCase recruiterUseCase) {
        this.candidateUseCase = candidateUseCase;
        this.recruiterUseCase = recruiterUseCase;
    }

     @PostMapping("register")
     public ResponseEntity<ApiResponse<CandidateResponse>> register(@Valid @RequestBody CandidateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("create success", candidateUseCase.create(request)));

     }

     @PostMapping("register/recruiter")
    public ResponseEntity<ApiResponse<RecruiterResponse>> create(@Valid @RequestBody RecruiterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("create success", recruiterUseCase.create(request)));
    }
}
