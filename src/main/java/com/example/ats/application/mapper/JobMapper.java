package com.example.ats.application.mapper;

import com.example.ats.application.dto.response.JobResponse;
import com.example.ats.domain.model.Job;
import com.example.ats.infrastructure.persistence.entity.JobEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface JobMapper {
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "category", ignore = true)
    JobResponse toResponse(Job job);

    @Mapping(target = "createdBy", ignore = true)
    Job toEntity(JobEntity entity);
}
