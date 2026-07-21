package com.example.ats.infrastructure.persistence.repository;

import com.example.ats.infrastructure.persistence.entity.ResumeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataResumeRepository extends JpaRepository<ResumeEntity, Long> {
    Page<ResumeEntity> findByCandidate_Id(Long candidateId, Pageable pageable);
}
