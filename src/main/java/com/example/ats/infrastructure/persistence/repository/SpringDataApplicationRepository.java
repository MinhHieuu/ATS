package com.example.ats.infrastructure.persistence.repository;

import com.example.ats.infrastructure.persistence.entity.ApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataApplicationRepository extends JpaRepository<ApplicationEntity, Long> {
    boolean existsByCandidate_IdAndJob_Id(Long candidateId, Long jobId);
    List<ApplicationEntity> findByCandidate_IdOrderByAppliedAtDesc(Long candidateId);
    List<ApplicationEntity> findByJob_IdOrderByAppliedAtDesc(Long jobId);
}
