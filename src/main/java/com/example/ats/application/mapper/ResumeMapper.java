package com.example.ats.application.mapper;

import com.example.ats.application.dto.response.ResumeResponse;
import com.example.ats.domain.model.Resume;
import com.example.ats.infrastructure.persistence.entity.ResumeEntity;
import org.springframework.stereotype.Component;

@Component
public class ResumeMapper {
    public ResumeResponse toResponse(Resume resume) {
        return resume == null ? null : new ResumeResponse(resume.getId(), resume.getCandidateId(),
                resume.getFileName(), resume.getFileUrl(), resume.getCreatedAt());
    }

    public Resume toDomain(ResumeEntity entity) {
        return entity == null ? null : new Resume(entity.getId(), entity.getCandidate().getId(),
                entity.getFileName(), entity.getFileUrl(), entity.getUploadedAt());
    }
}
