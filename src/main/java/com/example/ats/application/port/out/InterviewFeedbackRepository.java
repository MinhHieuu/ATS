package com.example.ats.application.port.out;

import com.example.ats.domain.model.InterviewFeedback;
import com.example.ats.domain.view.InterviewFeedbackView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InterviewFeedbackRepository {
    InterviewFeedbackView save(Long interviewId, Long reviewerId, InterviewFeedback feedback);
    Page<InterviewFeedbackView> findAll(Pageable pageable);
    InterviewFeedbackView findById(Long id);
    Page<InterviewFeedbackView> findByInterview(Long interviewId, Pageable pageable);
    Page<InterviewFeedbackView> findByJobCreatedBy(Long recruiterId, Pageable pageable);
    InterviewFeedbackView findByIdAndJobCreatedBy(Long id, Long recruiterId);
    Page<InterviewFeedbackView> findByInterviewAndJobCreatedBy(Long interviewId, Long recruiterId, Pageable pageable);
    boolean existsByInterviewAndReviewer(Long interviewId, Long reviewerId);
    void deleteById(Long id);
}
