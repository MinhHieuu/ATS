package com.example.ats.adapter.controller;

import com.example.ats.application.dto.request.CandidateRequest;
import com.example.ats.application.dto.response.CandidateResponse;
import com.example.ats.application.port.in.CandidateUseCase;
import com.example.ats.domain.model.ApiResponse;
import com.example.ats.domain.model.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidates")
public class CandidateController {
    private final CandidateUseCase candidateUseCase;

    public CandidateController(CandidateUseCase candidateUseCase) {
        this.candidateUseCase = candidateUseCase;
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<CandidateResponse>> create(@Valid @RequestBody CandidateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("create success", candidateUseCase.create(request)));
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<CandidateResponse>>> findAll() {
        return ResponseEntity.ok(new ApiResponse<>("success", candidateUseCase.findAll()));
    }

    @GetMapping("profile")
    public ResponseEntity<ApiResponse<CandidateResponse>> findProfille(Authentication authentication) {
        Long userId = ((User) authentication.getPrincipal()).getId();
        return ResponseEntity.ok(new ApiResponse("success", candidateUseCase.findByUserId(userId)));
    }

    @PatchMapping("")
    public ResponseEntity<ApiResponse<CandidateResponse>> update(Authentication authentication,
                                                                @Valid @RequestBody CandidateRequest request) {
        Long userId = ((User) authentication.getPrincipal()).getId();
        return ResponseEntity.ok(new ApiResponse<>("success", candidateUseCase.updateByUserId(request, userId)));
    }
}
