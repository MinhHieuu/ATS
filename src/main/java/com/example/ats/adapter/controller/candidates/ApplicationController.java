package com.example.ats.adapter.controller.candidates;

import com.example.ats.application.dto.request.JobApplicationRequest;
import com.example.ats.application.dto.response.JobApplicationResponse;
import com.example.ats.application.port.in.JobApplicationUseCase;
import com.example.ats.domain.model.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("candidateApplicationController")
@RequestMapping("/api/applications")
public class ApplicationController {
    private final JobApplicationUseCase jobApplicationUseCase;

    public ApplicationController(JobApplicationUseCase jobApplicationUseCase) {
        this.jobApplicationUseCase = jobApplicationUseCase;
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<JobApplicationResponse>> apply(Authentication authentication,
                                                                     @Valid @RequestBody JobApplicationRequest request) {
        // candidate.id dùng chung khóa chính với user.id (xem CandidateEntity#user @MapsId)
        Number userId = ((Jwt) authentication.getPrincipal()).getClaim("userId");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("create success", jobApplicationUseCase.apply(request, userId.longValue())));
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<Page<JobApplicationResponse>>> findMyApplications(
            Authentication authentication,
            @PageableDefault(size = 10, sort = "appliedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Number userId = ((Jwt) authentication.getPrincipal()).getClaim("userId");
        return ResponseEntity.ok(new ApiResponse<>("success",
                jobApplicationUseCase.findMyApplications(userId.longValue(), pageable)));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<JobApplicationResponse>> findById(Authentication authentication,
                                                                        @PathVariable Long id) {
        Number userId = ((Jwt) authentication.getPrincipal()).getClaim("userId");
        return ResponseEntity.ok(new ApiResponse<>("success",
                jobApplicationUseCase.findMyApplicationById(id, userId.longValue())));
    }

    @PatchMapping("{id}/withdraw")
    public ResponseEntity<ApiResponse<JobApplicationResponse>> withdraw(Authentication authentication,
                                                                        @PathVariable Long id) {
        Number userId = ((Jwt) authentication.getPrincipal()).getClaim("userId");
        return ResponseEntity.ok(new ApiResponse<>("success",
                jobApplicationUseCase.withdraw(id, userId.longValue())));
    }
}
