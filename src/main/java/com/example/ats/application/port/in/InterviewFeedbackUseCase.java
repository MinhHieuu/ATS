package com.example.ats.application.port.in;

import com.example.ats.application.dto.request.InterviewFeedbackRequest;
import com.example.ats.application.dto.response.InterviewFeedbackResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InterviewFeedbackUseCase {
    // Recruiter
    InterviewFeedbackResponse createByJobOwner(InterviewFeedbackRequest request, Long recruiterId);
    Page<InterviewFeedbackResponse> findByJobOwner(Long recruiterId, Pageable pageable);
    InterviewFeedbackResponse findByIdAndJobOwner(Long id, Long recruiterId);
    Page<InterviewFeedbackResponse> findByInterviewAndJobOwner(Long interviewId, Long recruiterId, Pageable pageable);
    InterviewFeedbackResponse updateByReviewer(Long id, InterviewFeedbackRequest request, Long recruiterId);
    void deleteByReviewer(Long id, Long recruiterId);

    // Admin
    Page<InterviewFeedbackResponse> findAll(Pageable pageable);
    InterviewFeedbackResponse findById(Long id);
    Page<InterviewFeedbackResponse> findByInterview(Long interviewId, Pageable pageable);
    void delete(Long id);
}
