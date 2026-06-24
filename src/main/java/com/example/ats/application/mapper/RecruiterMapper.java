package com.example.ats.application.mapper;

import com.example.ats.application.dto.response.RecruiterResponse;
import com.example.ats.domain.model.Recruiter;
import com.example.ats.infrastructure.persistence.entity.RecruiterEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RecruiterMapper {
    RecruiterResponse toResponse(Recruiter recruiter);
    Recruiter toEntity(RecruiterEntity entity);
}
