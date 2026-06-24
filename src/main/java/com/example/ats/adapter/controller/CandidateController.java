package com.example.ats.adapter.controller;

import com.example.ats.application.dto.request.CandidateRequest;
import com.example.ats.application.dto.response.CandidateResponse;
import com.example.ats.application.port.in.CandidateUseCase;
import com.example.ats.domain.model.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidates")
public class CandidateController {
    private final CandidateUseCase candidateUseCase;

    public CandidateController(CandidateUseCase candidateUseCase) {
        this.candidateUseCase = candidateUseCase;
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<CandidateResponse>>> findAll() {
        return ResponseEntity.ok(new ApiResponse<>("success", candidateUseCase.findAll()));
    }

    @GetMapping("profile")
    public ResponseEntity<ApiResponse<CandidateResponse>> findProfille(Long id) {
        return ResponseEntity.ok(new ApiResponse("success", candidateUseCase.findById(id)));
    }

    @PatchMapping("")
    public ResponseEntity<ApiResponse<CandidateResponse>> update(@Valid @RequestBody CandidateRequest request) {
        Long id = 1L;
        return ResponseEntity.ok(new ApiResponse<>("success", candidateUseCase.update(request, id)));
    }
}
