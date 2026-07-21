package com.example.ats.adapter.controller.admin;

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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("adminApplicationController")
@RequestMapping("/api/admin/applications")
public class AdminApplicationController {
    private final JobApplicationUseCase jobApplicationUseCase;

    public AdminApplicationController(JobApplicationUseCase jobApplicationUseCase) {
        this.jobApplicationUseCase = jobApplicationUseCase;
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<Page<JobApplicationResponse>>> findAll(
            @PageableDefault(size = 10, sort = "appliedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse<>("success", jobApplicationUseCase.findAll(pageable)));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<JobApplicationResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>("success", jobApplicationUseCase.findById(id)));
    }

    @GetMapping("job/{jobId}")
    public ResponseEntity<ApiResponse<Page<JobApplicationResponse>>> findByJob(
            @PathVariable Long jobId,
            @PageableDefault(size = 10, sort = "appliedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse<>("success", jobApplicationUseCase.findByJob(jobId, pageable)));
    }

    @GetMapping("candidate/{candidateId}")
    public ResponseEntity<ApiResponse<Page<JobApplicationResponse>>> findByCandidate(
            @PathVariable Long candidateId,
            @PageableDefault(size = 10, sort = "appliedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse<>("success",
                jobApplicationUseCase.findByCandidate(candidateId, pageable)));
    }

    @PatchMapping("{id}/status")
    public ResponseEntity<ApiResponse<JobApplicationResponse>> changeStatus(@PathVariable Long id,
                                                                            @Valid @RequestBody ApplicationStatusRequest request) {
        return ResponseEntity.ok(new ApiResponse<>("success", jobApplicationUseCase.updateStatus(id, request)));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        jobApplicationUseCase.delete(id);
        return ResponseEntity.ok(new ApiResponse<>("delete success", null));
    }
}
