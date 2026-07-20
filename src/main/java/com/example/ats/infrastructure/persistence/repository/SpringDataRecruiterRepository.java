package com.example.ats.infrastructure.persistence.repository;

import com.example.ats.infrastructure.persistence.entity.RecruiterEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SpringDataRecruiterRepository extends JpaRepository<RecruiterEntity, Long> {
    @EntityGraph(attributePaths = {"user", "company"})
    @Query(value = "select r from RecruiterEntity r",
            countQuery = "select count(r) from RecruiterEntity r")
    Page<RecruiterEntity> findAllWithUser(Pageable pageable);
}
