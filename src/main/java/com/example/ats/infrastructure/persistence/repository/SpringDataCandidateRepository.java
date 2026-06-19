package com.example.ats.infrastructure.persistence.repository;

import com.example.ats.infrastructure.persistence.entity.CandidateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataCandidateRepository extends JpaRepository<CandidateEntity, Long> {
    Optional<CandidateEntity> findByEmailIgnoreCase(String email);
}
