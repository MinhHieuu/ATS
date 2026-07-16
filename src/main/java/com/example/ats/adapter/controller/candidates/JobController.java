package com.example.ats.adapter.controller.candidates;

import com.example.ats.application.dto.response.JobResponse;
import com.example.ats.application.port.in.JobUseCase;
import com.example.ats.domain.model.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("candidateJobController")
@RequestMapping("/api/jobs")
public class JobController {
    private final JobUseCase jobUseCase;

    public JobController(JobUseCase jobUseCase) {
        this.jobUseCase = jobUseCase;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<JobResponse>>> findAll() {
        return ResponseEntity.ok(new ApiResponse<>("success", jobUseCase.findAllNotClosed()));
    }

    @GetMapping("search")
    public ResponseEntity<ApiResponse<List<JobResponse>>> searchByTitle(@RequestParam String title) {
        return ResponseEntity.ok(new ApiResponse<>("success", jobUseCase.searchByTitleNotClosed(title)));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<JobResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>("success", jobUseCase.findByIdNotClosed(id)));
    }


}
