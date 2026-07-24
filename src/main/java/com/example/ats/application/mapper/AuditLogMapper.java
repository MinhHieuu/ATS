package com.example.ats.application.mapper;

import com.example.ats.application.dto.response.AuditLogResponse;
import com.example.ats.domain.model.AuditLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuditLogMapper {
    @Mapping(target = "actorName", ignore = true)
    AuditLogResponse toResponse(AuditLog log);
}
