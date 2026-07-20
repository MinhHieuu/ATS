package com.example.ats.adapter.controller.candidates;

import com.example.ats.application.dto.request.CandidateRequest;
import com.example.ats.application.dto.response.CandidateResponse;
import com.example.ats.application.port.in.CandidateUseCase;
import com.example.ats.domain.model.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ApiResponse<Page<CandidateResponse>>> findAll(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse<>("success", candidateUseCase.findAll(pageable)));
    }

    @GetMapping("profile")
    public ResponseEntity<ApiResponse<CandidateResponse>> findProfille(Authentication authentication) {
        Number userId = ((Jwt) authentication.getPrincipal()).getClaim("userId");
        return ResponseEntity.ok(new ApiResponse("success", candidateUseCase.findById(userId.longValue())));
    }

    @PatchMapping("")
    public ResponseEntity<ApiResponse<CandidateResponse>> update(Authentication authentication,
                                                                @Valid @RequestBody CandidateRequest request) {
        // candidate.id dùng chung khóa chính với user.id (xem CandidateEntity#user @MapsId)
        Number userId = ((Jwt) authentication.getPrincipal()).getClaim("userId");
        return ResponseEntity.ok(new ApiResponse<>("success", candidateUseCase.update(request, userId.longValue())));
    }
}
