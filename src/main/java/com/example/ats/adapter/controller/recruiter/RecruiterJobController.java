package com.example.ats.adapter.controller.recruiter;

import com.example.ats.application.dto.request.JobRequest;
import com.example.ats.application.dto.response.JobResponse;
import com.example.ats.application.port.in.JobUseCase;
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

@RequestMapping("/api/recruiter/jobs")
@RestController("recruiterJobController")
public class RecruiterJobController {
    private final JobUseCase jobUseCase;
    public RecruiterJobController(JobUseCase jobUseCase) {
        this.jobUseCase = jobUseCase;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<JobResponse>> create(Authentication authentication,
                                                           @Valid @RequestBody JobRequest request) {
        Number userId = ((Jwt) authentication.getPrincipal()).getClaim("userId");
        request.setCreatedBy(userId.longValue());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("create success", jobUseCase.createByRecruiter(request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<JobResponse>>> findMyJobs(
            Authentication authentication,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        Number userId = ((Jwt) authentication.getPrincipal()).getClaim("userId");
        return ResponseEntity.ok(new ApiResponse<>("success",
                jobUseCase.findByCreatedBy(userId.longValue(), pageable)));
    }

    @GetMapping("search")
    public ResponseEntity<ApiResponse<Page<JobResponse>>> searchMyJobsByTitle(
            Authentication authentication,
            @RequestParam String title,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        Number userId = ((Jwt) authentication.getPrincipal()).getClaim("userId");
        return ResponseEntity.ok(new ApiResponse<>("success",
                jobUseCase.searchByTitleAndCreatedBy(title, userId.longValue(), pageable)));
    }

    @GetMapping("category/{categoryId}")
    public ResponseEntity<ApiResponse<Page<JobResponse>>> findMyJobsByCategory(
            Authentication authentication,
            @PathVariable Long categoryId,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        Number userId = ((Jwt) authentication.getPrincipal()).getClaim("userId");
        return ResponseEntity.ok(new ApiResponse<>("success",
                jobUseCase.findByCategoryAndCreatedBy(categoryId, userId.longValue(), pageable)));
    }

    @PatchMapping("{id}/deactivate")
    public ResponseEntity<ApiResponse<JobResponse>> deactivate(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>("success", jobUseCase.deactivate(id)));
    }

    @PatchMapping("{id}/active")
    public ResponseEntity<ApiResponse<JobResponse>> active(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>("success", jobUseCase.activate(id)));
    }


    @PatchMapping("{id}")
    public ResponseEntity<ApiResponse<JobResponse>> update(@PathVariable Long id,
                                                           @Valid @RequestBody JobRequest request) {
        return ResponseEntity.ok(new ApiResponse<>("success", jobUseCase.updateByRecruiter(id, request)));
    }
}
