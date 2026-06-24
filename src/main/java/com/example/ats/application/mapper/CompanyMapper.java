package com.example.ats.application.mapper;

import com.example.ats.application.dto.response.CompanyResponse;
import com.example.ats.domain.model.Company;
import com.example.ats.infrastructure.persistence.entity.CompanyEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CompanyMapper {
    CompanyResponse toResponse(Company company);
    Company toEntity(CompanyEntity entity);
}
