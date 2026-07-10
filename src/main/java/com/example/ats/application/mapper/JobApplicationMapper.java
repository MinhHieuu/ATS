package com.example.ats.application.mapper;

import com.example.ats.application.dto.response.JobApplicationResponse;
import com.example.ats.domain.model.JobApplication;
import com.example.ats.infrastructure.persistence.entity.ApplicationEntity;
import org.springframework.stereotype.Component;

@Component
public class JobApplicationMapper {
    public JobApplicationResponse toResponse(JobApplication application) {
        if (application == null) {
            return null;
        }
        return new JobApplicationResponse(application.getId(), application.getCandidateId(), application.getJobId(),
                application.getStageId(), application.getStatus(), application.getSource(),
                application.getExpectedSalary(), application.getNote(), application.getAppliedAt(),
                application.getUpdatedAt());
    }

    public JobApplication toDomain(ApplicationEntity entity) {
        if (entity == null) {
            return null;
        }
        Long stageId = entity.getStage() == null ? null : entity.getStage().getId();
        return new JobApplication(entity.getId(), entity.getCandidate().getId(), entity.getJob().getId(), stageId,
                entity.getStatus(), entity.getSource(), entity.getExpectedSalary(), entity.getNote(), entity.getAppliedAt(),
                entity.getUpdatedAt());
    }
}
