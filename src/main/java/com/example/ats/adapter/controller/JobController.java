package com.example.ats.adapter.controller;

import com.example.ats.application.dto.request.JobRequest;
import com.example.ats.application.dto.response.JobResponse;
import com.example.ats.application.port.in.JobUseCase;
import com.example.ats.domain.model.ApiResponse;
import com.example.ats.domain.model.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {
    private final JobUseCase jobUseCase;

    public JobController(JobUseCase jobUseCase) {
        this.jobUseCase = jobUseCase;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<JobResponse>> create(Authentication authentication,
                                                           @Valid @RequestBody JobRequest request) {
        Long userId = ((User) authentication.getPrincipal()).getId();
        request.setCreatedBy(userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("create success", jobUseCase.create(request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<JobResponse>>> findAll() {
        return ResponseEntity.ok(new ApiResponse<>("success", jobUseCase.findAll()));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<JobResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>("success", jobUseCase.findById(id)));
    }

    @PatchMapping("{id}")
    public ResponseEntity<ApiResponse<JobResponse>> update(@PathVariable Long id,
                                                           @Valid @RequestBody JobRequest request) {
        return ResponseEntity.ok(new ApiResponse<>("success", jobUseCase.update(id, request)));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        jobUseCase.delete(id);
        return ResponseEntity.ok(new ApiResponse<>("delete success", null));
    }

    @PatchMapping("{id}/deactivate")
    public ResponseEntity<ApiResponse<JobResponse>> deactivate(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>("success", jobUseCase.deactivate(id)));
    }

    @PatchMapping("{id}/active")
    public ResponseEntity<ApiResponse<JobResponse>> active(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>("success", jobUseCase.activate(id)));
    }
}
