package com.example.ats.application.mapper;

import com.example.ats.application.dto.response.ApplicationStageResponse;
import com.example.ats.domain.model.ApplicationStage;
import com.example.ats.infrastructure.persistence.entity.ApplicationStageEntity;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStageMapper {
    public ApplicationStageResponse toResponse(ApplicationStage stage) {
        return stage == null ? null : new ApplicationStageResponse(stage.getId(), stage.getName(), stage.getPosition());
    }

    public ApplicationStage toDomain(ApplicationStageEntity entity) {
        return entity == null ? null : new ApplicationStage(entity.getId(), entity.getName(), entity.getPosition());
    }
}
