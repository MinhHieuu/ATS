package com.example.ats.adapter.controller.recruiter;

import com.example.ats.application.dto.request.InterviewFeedbackRequest;
import com.example.ats.application.dto.response.InterviewFeedbackResponse;
import com.example.ats.application.port.in.InterviewFeedbackUseCase;
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
@RequestMapping("/api/recruiter/interview-feedbacks")
public class RecruiterInterviewFeedbackController {
    private final InterviewFeedbackUseCase interviewFeedbackUseCase;

    public RecruiterInterviewFeedbackController(InterviewFeedbackUseCase interviewFeedbackUseCase) {
        this.interviewFeedbackUseCase = interviewFeedbackUseCase;
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<InterviewFeedbackResponse>> create(
            Authentication authentication,
            @Valid @RequestBody InterviewFeedbackRequest request) {
        Number userId = ((Jwt) authentication.getPrincipal()).getClaim("userId");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("create success",
                        interviewFeedbackUseCase.createByJobOwner(request, userId.longValue())));
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<Page<InterviewFeedbackResponse>>> findAll(
            Authentication authentication,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Number userId = ((Jwt) authentication.getPrincipal()).getClaim("userId");
        return ResponseEntity.ok(new ApiResponse<>("success",
                interviewFeedbackUseCase.findByJobOwner(userId.longValue(), pageable)));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<InterviewFeedbackResponse>> findById(
            Authentication authentication,
            @PathVariable Long id) {
        Number userId = ((Jwt) authentication.getPrincipal()).getClaim("userId");
        return ResponseEntity.ok(new ApiResponse<>("success",
                interviewFeedbackUseCase.findByIdAndJobOwner(id, userId.longValue())));
    }

    @GetMapping("interview/{interviewId}")
    public ResponseEntity<ApiResponse<Page<InterviewFeedbackResponse>>> findByInterview(
            Authentication authentication,
            @PathVariable Long interviewId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Number userId = ((Jwt) authentication.getPrincipal()).getClaim("userId");
        return ResponseEntity.ok(new ApiResponse<>("success",
                interviewFeedbackUseCase.findByInterviewAndJobOwner(interviewId, userId.longValue(), pageable)));
    }

    @PatchMapping("{id}")
    public ResponseEntity<ApiResponse<InterviewFeedbackResponse>> update(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody InterviewFeedbackRequest request) {
        Number userId = ((Jwt) authentication.getPrincipal()).getClaim("userId");
        return ResponseEntity.ok(new ApiResponse<>("success",
                interviewFeedbackUseCase.updateByReviewer(id, request, userId.longValue())));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            Authentication authentication,
            @PathVariable Long id) {
        Number userId = ((Jwt) authentication.getPrincipal()).getClaim("userId");
        interviewFeedbackUseCase.deleteByReviewer(id, userId.longValue());
        return ResponseEntity.ok(new ApiResponse<>("delete success", null));
    }
}
