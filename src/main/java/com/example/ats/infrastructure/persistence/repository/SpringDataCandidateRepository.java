package com.example.ats.infrastructure.persistence.repository;

import com.example.ats.infrastructure.persistence.entity.CandidateEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SpringDataCandidateRepository extends JpaRepository<CandidateEntity, Long> {
    @EntityGraph(attributePaths = "user")
    @Query(value = "select c from CandidateEntity c",
            countQuery = "select count(c) from CandidateEntity c")
    Page<CandidateEntity> findAllWithUser(Pageable pageable);
}
