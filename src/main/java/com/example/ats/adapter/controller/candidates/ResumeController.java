package com.example.ats.adapter.controller.candidates;

import com.example.ats.application.dto.request.ResumeRequest;
import com.example.ats.application.dto.response.ResumeResponse;
import com.example.ats.application.port.in.ResumeUseCase;
import com.example.ats.domain.model.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resumes")
public class ResumeController {
    private final ResumeUseCase useCase;

    public ResumeController(ResumeUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ResumeResponse>> create(@Valid @RequestBody ResumeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("create success", useCase.create(request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ResumeResponse>>> findAll(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse<>("success", useCase.findAll(pageable)));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<ResumeResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>("success", useCase.findById(id)));
    }

    @GetMapping("candidate/{candidateId}")
    public ResponseEntity<ApiResponse<Page<ResumeResponse>>> findByCandidate(
            @PathVariable Long candidateId,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse<>("success", useCase.findByCandidate(candidateId, pageable)));
    }

    @PatchMapping("{id}")
    public ResponseEntity<ApiResponse<ResumeResponse>> update(@PathVariable Long id,
                                                              @Valid @RequestBody ResumeRequest request) {
        return ResponseEntity.ok(new ApiResponse<>("success", useCase.update(id, request)));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        useCase.delete(id);
        return ResponseEntity.ok(new ApiResponse<>("delete success", null));
    }
}
