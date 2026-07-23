package com.example.ats.adapter.controller.admin;

import com.example.ats.application.dto.request.InterviewRequest;
import com.example.ats.application.dto.request.InterviewResultRequest;
import com.example.ats.application.dto.response.InterviewResponse;
import com.example.ats.application.port.in.InterviewUseCase;
import com.example.ats.domain.model.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("adminInterviewController")
@RequestMapping("/api/admin/interviews")
public class AdminInterviewController {
    private final InterviewUseCase interviewUseCase;

    public AdminInterviewController(InterviewUseCase interviewUseCase) {
        this.interviewUseCase = interviewUseCase;
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<InterviewResponse>> create(
            @Valid @RequestBody InterviewRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("create success", interviewUseCase.create(request)));
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<Page<InterviewResponse>>> findAll(
            @PageableDefault(size = 10, sort = "scheduledAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse<>("success", interviewUseCase.findAll(pageable)));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<InterviewResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>("success", interviewUseCase.findById(id)));
    }

    @GetMapping("application/{applicationId}")
    public ResponseEntity<ApiResponse<Page<InterviewResponse>>> findByApplication(
            @PathVariable Long applicationId,
            @PageableDefault(size = 10, sort = "scheduledAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse<>("success",
                interviewUseCase.findByApplication(applicationId, pageable)));
    }

    @PatchMapping("{id}")
    public ResponseEntity<ApiResponse<InterviewResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody InterviewRequest request) {
        return ResponseEntity.ok(new ApiResponse<>("success", interviewUseCase.update(id, request)));
    }

    @PatchMapping("{id}/result")
    public ResponseEntity<ApiResponse<InterviewResponse>> updateResult(
            @PathVariable Long id,
            @Valid @RequestBody InterviewResultRequest request) {
        return ResponseEntity.ok(new ApiResponse<>("success", interviewUseCase.updateResult(id, request)));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        interviewUseCase.delete(id);
        return ResponseEntity.ok(new ApiResponse<>("delete success", null));
    }
}
