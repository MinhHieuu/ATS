package com.example.ats.application.mapper;

import com.example.ats.application.dto.response.JobResponse;
import com.example.ats.domain.model.Job;
import com.example.ats.infrastructure.persistence.entity.JobEntity;
import org.springframework.stereotype.Component;

@Component
public class JobMapper {
    public JobResponse toResponse(Job job) {
        if (job == null) {
            return null;
        }
        return new JobResponse(job.getId(), job.getTitle(), job.getDescription(), job.getRequirements(),
                job.getLocation(), job.getEmploymentType(), job.getSalaryMin(), job.getSalaryMax(),
                job.getStatus(), job.getCreatedBy(), job.getCreatedAt(), job.getUpdatedAt());
    }

    public Job toDomain(JobEntity entity) {
        if (entity == null) {
            return null;
        }
        Long createdBy = entity.getCreatedBy() == null ? null : entity.getCreatedBy().getId();
        return new Job(entity.getId(), entity.getTitle(), entity.getDescription(), entity.getRequirements(),
                entity.getLocation(), entity.getEmploymentType(), entity.getSalaryMin(), entity.getSalaryMax(),
                entity.getStatus(), createdBy, entity.getCreatedAt(), entity.getUpdatedAt());
    }
}
