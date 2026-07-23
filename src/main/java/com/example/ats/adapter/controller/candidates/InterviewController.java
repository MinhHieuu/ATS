package com.example.ats.adapter.controller.candidates;

import com.example.ats.application.dto.response.InterviewResponse;
import com.example.ats.application.port.in.InterviewUseCase;
import com.example.ats.domain.model.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("candidateInterviewController")
@RequestMapping("/api/interviews")
public class InterviewController {
    private final InterviewUseCase interviewUseCase;

    public InterviewController(InterviewUseCase interviewUseCase) {
        this.interviewUseCase = interviewUseCase;
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<Page<InterviewResponse>>> findMyInterviews(
            Authentication authentication,
            @PageableDefault(size = 10, sort = "scheduledAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Number userId = ((Jwt) authentication.getPrincipal()).getClaim("userId");
        return ResponseEntity.ok(new ApiResponse<>("success",
                interviewUseCase.findMyInterviews(userId.longValue(), pageable)));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<InterviewResponse>> findById(
            Authentication authentication,
            @PathVariable Long id) {
        Number userId = ((Jwt) authentication.getPrincipal()).getClaim("userId");
        return ResponseEntity.ok(new ApiResponse<>("success",
                interviewUseCase.findMyInterviewById(id, userId.longValue())));
    }
}
