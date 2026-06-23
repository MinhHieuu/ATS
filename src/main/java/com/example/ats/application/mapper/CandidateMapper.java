package com.example.ats.application.mapper;

import com.example.ats.application.dto.response.CandidateResponse;
import com.example.ats.domain.model.Candidate;
import com.example.ats.infrastructure.persistence.entity.CandidateEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CandidateMapper {
    CandidateResponse toResponse(Candidate candidate);
    Candidate toEntity(CandidateEntity entity);
}
