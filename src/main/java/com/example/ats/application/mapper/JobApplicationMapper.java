package com.example.ats.application.mapper;

import com.example.ats.application.dto.response.JobApplicationResponse;
import com.example.ats.domain.model.JobApplication;
import com.example.ats.infrastructure.persistence.entity.ApplicationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface JobApplicationMapper {
    @Mapping(target = "candidate", ignore = true)
    @Mapping(target = "job", ignore = true)
    @Mapping(target = "resume", ignore = true)
    JobApplicationResponse toResponse(JobApplication application);

    @Mapping(source = "candidate.id", target = "candidateId")
    @Mapping(source = "job.id", target = "jobId")
    @Mapping(source = "resume.id", target = "resumeId")
    JobApplication toEntity(ApplicationEntity entity);
}
