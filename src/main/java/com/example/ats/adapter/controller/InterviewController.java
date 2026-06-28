package com.example.ats.adapter.controller;

import com.example.ats.application.dto.request.InterviewRequest;
import com.example.ats.application.dto.request.InterviewStatusRequest;
import com.example.ats.application.dto.response.InterviewResponse;
import com.example.ats.application.port.in.InterviewUseCase;
import com.example.ats.domain.model.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interviews")
public class InterviewController {
    private final InterviewUseCase useCase;

    public InterviewController(InterviewUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<InterviewResponse>> create(@Valid @RequestBody InterviewRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("create success", useCase.create(request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<InterviewResponse>>> findAll() {
        return ResponseEntity.ok(new ApiResponse<>("success", useCase.findAll()));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<InterviewResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>("success", useCase.findById(id)));
    }

    @GetMapping("application/{applicationId}")
    public ResponseEntity<ApiResponse<List<InterviewResponse>>> findByApplication(@PathVariable Long applicationId) {
        return ResponseEntity.ok(new ApiResponse<>("success", useCase.findByApplication(applicationId)));
    }

    @PatchMapping("{id}")
    public ResponseEntity<ApiResponse<InterviewResponse>> update(@PathVariable Long id,
                                                                 @Valid @RequestBody InterviewRequest request) {
        return ResponseEntity.ok(new ApiResponse<>("success", useCase.update(id, request)));
    }

    @PatchMapping("{id}/status")
    public ResponseEntity<ApiResponse<InterviewResponse>> changeStatus(@PathVariable Long id,
                                                                       @Valid @RequestBody InterviewStatusRequest request) {
        return ResponseEntity.ok(new ApiResponse<>("success", useCase.changeStatus(id, request)));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        useCase.delete(id);
        return ResponseEntity.ok(new ApiResponse<>("delete success", null));
    }
}
