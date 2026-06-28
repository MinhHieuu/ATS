package com.example.ats.adapter.controller;

import com.example.ats.application.dto.request.ApplicationStatusRequest;
import com.example.ats.application.dto.request.JobApplicationRequest;
import com.example.ats.application.dto.response.JobApplicationResponse;
import com.example.ats.application.port.in.JobApplicationUseCase;
import com.example.ats.domain.model.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class JobApplicationController {
    private final JobApplicationUseCase useCase;

    public JobApplicationController(JobApplicationUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<JobApplicationResponse>> create(@Valid @RequestBody JobApplicationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("create success", useCase.create(request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<JobApplicationResponse>>> findAll(@RequestParam(required = false) Long candidateId,
                                                                             @RequestParam(required = false) Long jobId) {
        if (candidateId != null) {
            return ResponseEntity.ok(new ApiResponse<>("success", useCase.findByCandidate(candidateId)));
        }
        if (jobId != null) {
            return ResponseEntity.ok(new ApiResponse<>("success", useCase.findByJob(jobId)));
        }
        return ResponseEntity.ok(new ApiResponse<>("success", useCase.findAll()));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<JobApplicationResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>("success", useCase.findById(id)));
    }

    @GetMapping("candidate/{candidateId}")
    public ResponseEntity<ApiResponse<List<JobApplicationResponse>>> findByCandidate(@PathVariable Long candidateId) {
        return ResponseEntity.ok(new ApiResponse<>("success", useCase.findByCandidate(candidateId)));
    }

    @GetMapping("job/{jobId}")
    public ResponseEntity<ApiResponse<List<JobApplicationResponse>>> findByJob(@PathVariable Long jobId) {
        return ResponseEntity.ok(new ApiResponse<>("success", useCase.findByJob(jobId)));
    }

    @PatchMapping("{id}")
    public ResponseEntity<ApiResponse<JobApplicationResponse>> update(@PathVariable Long id,
                                                                      @Valid @RequestBody JobApplicationRequest request) {
        return ResponseEntity.ok(new ApiResponse<>("success", useCase.update(id, request)));
    }

    @PatchMapping("{id}/status")
    public ResponseEntity<ApiResponse<JobApplicationResponse>> changeStatus(@PathVariable Long id,
                                                                            @Valid @RequestBody ApplicationStatusRequest request) {
        return ResponseEntity.ok(new ApiResponse<>("success", useCase.changeStatus(id, request)));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        useCase.delete(id);
        return ResponseEntity.ok(new ApiResponse<>("delete success", null));
    }
}
