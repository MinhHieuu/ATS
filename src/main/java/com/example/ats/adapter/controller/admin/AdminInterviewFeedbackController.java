package com.example.ats.adapter.controller.admin;

import com.example.ats.application.dto.response.InterviewFeedbackResponse;
import com.example.ats.application.port.in.InterviewFeedbackUseCase;
import com.example.ats.domain.model.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("adminInterviewFeedbackController")
@RequestMapping("/api/admin/interview-feedbacks")
public class AdminInterviewFeedbackController {
    private final InterviewFeedbackUseCase interviewFeedbackUseCase;

    public AdminInterviewFeedbackController(InterviewFeedbackUseCase interviewFeedbackUseCase) {
        this.interviewFeedbackUseCase = interviewFeedbackUseCase;
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<Page<InterviewFeedbackResponse>>> findAll(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse<>("success", interviewFeedbackUseCase.findAll(pageable)));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<InterviewFeedbackResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>("success", interviewFeedbackUseCase.findById(id)));
    }

    @GetMapping("interview/{interviewId}")
    public ResponseEntity<ApiResponse<Page<InterviewFeedbackResponse>>> findByInterview(
            @PathVariable Long interviewId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse<>("success",
                interviewFeedbackUseCase.findByInterview(interviewId, pageable)));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        interviewFeedbackUseCase.delete(id);
        return ResponseEntity.ok(new ApiResponse<>("delete success", null));
    }
}
