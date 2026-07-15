package com.example.ats.infrastructure.persistence.repository;

import com.example.ats.infrastructure.persistence.entity.CandidateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SpringDataCandidateRepository extends JpaRepository<CandidateEntity, Long> {
    @Query("select candidate from CandidateEntity candidate join fetch candidate.user")
    List<CandidateEntity> findAllWithUser();
}
