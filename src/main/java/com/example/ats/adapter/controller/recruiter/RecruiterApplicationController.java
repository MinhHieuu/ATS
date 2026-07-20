package com.example.ats.adapter.controller.recruiter;

import com.example.ats.application.dto.request.ApplicationStatusRequest;
import com.example.ats.application.dto.response.JobApplicationResponse;
import com.example.ats.application.port.in.JobApplicationUseCase;
import com.example.ats.domain.model.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/recruiter/applications")
public class RecruiterApplicationController {
    private final JobApplicationUseCase jobApplicationUseCase;

    public RecruiterApplicationController(JobApplicationUseCase jobApplicationUseCase) {
        this.jobApplicationUseCase = jobApplicationUseCase;
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<Page<JobApplicationResponse>>> findAll(
            Authentication authentication,
            @PageableDefault(size = 10, sort = "appliedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Number userId = ((Jwt) authentication.getPrincipal()).getClaim("userId");
        return ResponseEntity.ok(new ApiResponse<>("success",
                jobApplicationUseCase.findByJobOwner(userId.longValue(), pageable)));
    }

    @GetMapping("job/{jobId}")
    public ResponseEntity<ApiResponse<Page<JobApplicationResponse>>> findByJob(
            Authentication authentication,
            @PathVariable Long jobId,
            @PageableDefault(size = 10, sort = "appliedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Number userId = ((Jwt) authentication.getPrincipal()).getClaim("userId");
        return ResponseEntity.ok(new ApiResponse<>("success",
                jobApplicationUseCase.findByJobAndJobOwner(jobId, userId.longValue(), pageable)));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<JobApplicationResponse>> findById(Authentication authentication,
                                                                        @PathVariable Long id) {
        Number userId = ((Jwt) authentication.getPrincipal()).getClaim("userId");
        return ResponseEntity.ok(new ApiResponse<>("success",
                jobApplicationUseCase.findByIdAndJobOwner(id, userId.longValue())));
    }

    @PatchMapping("{id}/status")
    public ResponseEntity<ApiResponse<JobApplicationResponse>> changeStatus(Authentication authentication,
                                                                            @PathVariable Long id,
                                                                            @Valid @RequestBody ApplicationStatusRequest request) {
        Number userId = ((Jwt) authentication.getPrincipal()).getClaim("userId");
        return ResponseEntity.ok(new ApiResponse<>("success",
                jobApplicationUseCase.updateStatusByJobOwner(id, request, userId.longValue())));
    }
}
