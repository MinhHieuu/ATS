package com.example.ats.infrastructure.persistence.repository;

import com.example.ats.infrastructure.persistence.entity.ResumeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SpringDataResumeRepository extends JpaRepository<ResumeEntity, Long> {
    List<ResumeEntity> findByCandidate_Id(Long candidateId);
}
