package com.example.ats.application.port.in;

import com.example.ats.application.dto.request.InterviewFeedbackRequest;
import com.example.ats.application.dto.response.InterviewFeedbackResponse;

import java.util.List;

public interface InterviewFeedbackUseCase {
    InterviewFeedbackResponse create(InterviewFeedbackRequest request);
    InterviewFeedbackResponse update(Long id, InterviewFeedbackRequest request);
    InterviewFeedbackResponse findById(Long id);
    List<InterviewFeedbackResponse> findAll();
    void delete(Long id);
}
