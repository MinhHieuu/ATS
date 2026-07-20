package com.example.ats.adapter.controller.candidates;

import com.example.ats.application.dto.response.JobResponse;
import com.example.ats.application.port.in.JobUseCase;
import com.example.ats.domain.model.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("candidateJobController")
@RequestMapping("/api/jobs")
public class JobController {
    private final JobUseCase jobUseCase;

    public JobController(JobUseCase jobUseCase) {
        this.jobUseCase = jobUseCase;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<JobResponse>>> findAll(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse<>("success", jobUseCase.findAllNotClosed(pageable)));
    }

    @GetMapping("search")
    public ResponseEntity<ApiResponse<Page<JobResponse>>> searchByTitle(
            @RequestParam String title,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse<>("success", jobUseCase.searchByTitleNotClosed(title, pageable)));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<JobResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>("success", jobUseCase.findByIdNotClosed(id)));
    }


}
