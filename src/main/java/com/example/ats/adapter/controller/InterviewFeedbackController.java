package com.example.ats.adapter.controller;

import com.example.ats.application.dto.request.InterviewFeedbackRequest;
import com.example.ats.application.dto.response.InterviewFeedbackResponse;
import com.example.ats.application.port.in.InterviewFeedbackUseCase;
import com.example.ats.domain.model.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interview-feedbacks")
public class InterviewFeedbackController {
    private final InterviewFeedbackUseCase useCase;

    public InterviewFeedbackController(InterviewFeedbackUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<InterviewFeedbackResponse>> create(@Valid @RequestBody InterviewFeedbackRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("create success", useCase.create(request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<InterviewFeedbackResponse>>> findAll() {
        return ResponseEntity.ok(new ApiResponse<>("success", useCase.findAll()));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<InterviewFeedbackResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>("success", useCase.findById(id)));
    }

    @PatchMapping("{id}")
    public ResponseEntity<ApiResponse<InterviewFeedbackResponse>> update(@PathVariable Long id,
                                                                         @Valid @RequestBody InterviewFeedbackRequest request) {
        return ResponseEntity.ok(new ApiResponse<>("success", useCase.update(id, request)));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        useCase.delete(id);
        return ResponseEntity.ok(new ApiResponse<>("delete success", null));
    }
}
