package com.example.ats.application.mapper;

import com.example.ats.application.dto.response.InterviewFeedbackResponse;
import com.example.ats.domain.model.InterviewFeedback;
import com.example.ats.infrastructure.persistence.entity.InterviewFeedbackEntity;
import org.springframework.stereotype.Component;

@Component
public class InterviewFeedbackMapper {
    public InterviewFeedbackResponse toResponse(InterviewFeedback feedback) {
        return feedback == null ? null : new InterviewFeedbackResponse(feedback.getId(), feedback.getInterviewId(),
                feedback.getRecruiterId(), feedback.getRating(), feedback.getComment(), feedback.getRecommendation(),
                feedback.getCreatedAt());
    }

    public InterviewFeedback toDomain(InterviewFeedbackEntity entity) {
        return entity == null ? null : new InterviewFeedback(entity.getId(), entity.getInterview().getId(),
                entity.getReviewer().getId(), entity.getRating(), entity.getComment(), entity.getRecommendation(),
                entity.getCreatedAt());
    }
}
