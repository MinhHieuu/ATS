package com.example.ats.adapter.controller.recruiter;

import com.example.ats.application.dto.request.InterviewRequest;
import com.example.ats.application.dto.request.InterviewResultRequest;
import com.example.ats.application.dto.response.InterviewResponse;
import com.example.ats.application.port.in.InterviewUseCase;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recruiter/interviews")
public class RecruiterInterviewController {
    private final InterviewUseCase interviewUseCase;

    public RecruiterInterviewController(InterviewUseCase interviewUseCase) {
        this.interviewUseCase = interviewUseCase;
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<InterviewResponse>> create(
            Authentication authentication,
            @Valid @RequestBody InterviewRequest request) {
        Number userId = ((Jwt) authentication.getPrincipal()).getClaim("userId");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("create success",
                        interviewUseCase.createByJobOwner(request, userId.longValue())));
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<Page<InterviewResponse>>> findAll(
            Authentication authentication,
            @PageableDefault(size = 10, sort = "scheduledAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Number userId = ((Jwt) authentication.getPrincipal()).getClaim("userId");
        return ResponseEntity.ok(new ApiResponse<>("success",
                interviewUseCase.findByJobOwner(userId.longValue(), pageable)));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<InterviewResponse>> findById(
            Authentication authentication,
            @PathVariable Long id) {
        Number userId = ((Jwt) authentication.getPrincipal()).getClaim("userId");
        return ResponseEntity.ok(new ApiResponse<>("success",
                interviewUseCase.findByIdAndJobOwner(id, userId.longValue())));
    }

    @GetMapping("application/{applicationId}")
    public ResponseEntity<ApiResponse<Page<InterviewResponse>>> findByApplication(
            Authentication authentication,
            @PathVariable Long applicationId,
            @PageableDefault(size = 10, sort = "scheduledAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Number userId = ((Jwt) authentication.getPrincipal()).getClaim("userId");
        return ResponseEntity.ok(new ApiResponse<>("success",
                interviewUseCase.findByApplicationAndJobOwner(applicationId, userId.longValue(), pageable)));
    }

    @PatchMapping("{id}")
    public ResponseEntity<ApiResponse<InterviewResponse>> update(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody InterviewRequest request) {
        Number userId = ((Jwt) authentication.getPrincipal()).getClaim("userId");
        return ResponseEntity.ok(new ApiResponse<>("success",
                interviewUseCase.updateByJobOwner(id, request, userId.longValue())));
    }

    @PatchMapping("{id}/result")
    public ResponseEntity<ApiResponse<InterviewResponse>> updateResult(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody InterviewResultRequest request) {
        Number userId = ((Jwt) authentication.getPrincipal()).getClaim("userId");
        return ResponseEntity.ok(new ApiResponse<>("success",
                interviewUseCase.updateResultByJobOwner(id, request, userId.longValue())));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            Authentication authentication,
            @PathVariable Long id) {
        Number userId = ((Jwt) authentication.getPrincipal()).getClaim("userId");
        interviewUseCase.deleteByJobOwner(id, userId.longValue());
        return ResponseEntity.ok(new ApiResponse<>("delete success", null));
    }
}
