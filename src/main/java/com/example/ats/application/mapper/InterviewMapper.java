package com.example.ats.application.mapper;

import com.example.ats.application.dto.response.InterviewResponse;
import com.example.ats.domain.model.Interview;
import com.example.ats.domain.model.InterviewStatus;
import com.example.ats.infrastructure.persistence.entity.InterviewEntity;
import org.springframework.stereotype.Component;

@Component
public class InterviewMapper {
    public InterviewResponse toResponse(Interview interview) {
        if (interview == null) {
            return null;
        }
        return new InterviewResponse(interview.getId(), interview.getJobApplicationId(), interview.getInterviewerId(),
                interview.getTitle(), interview.getInterviewTime(), interview.getLocation(), interview.getStatus(),
                interview.getCreatedAt());
    }

    public Interview toDomain(InterviewEntity entity) {
        if (entity == null) {
            return null;
        }
        Long interviewerId = entity.getInterviewer() == null ? null : entity.getInterviewer().getId();
        InterviewStatus status = entity.getStatus() == null ? null : InterviewStatus.valueOf(entity.getStatus());
        return new Interview(entity.getId(), entity.getApplication().getId(), interviewerId, entity.getTitle(),
                entity.getInterviewTime(), entity.getLocation(), status, entity.getCreatedAt());
    }
}
