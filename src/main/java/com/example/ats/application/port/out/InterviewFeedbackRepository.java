package com.example.ats.application.port.out;

import com.example.ats.domain.model.InterviewFeedback;

import java.util.List;

public interface InterviewFeedbackRepository {
    InterviewFeedback save(InterviewFeedback feedback);
    InterviewFeedback findById(Long id);
    List<InterviewFeedback> findAll();
    void deleteById(Long id);
}
