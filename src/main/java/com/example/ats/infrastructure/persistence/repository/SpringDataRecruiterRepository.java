package com.example.ats.infrastructure.persistence.repository;

import com.example.ats.infrastructure.persistence.entity.RecruiterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SpringDataRecruiterRepository extends JpaRepository<RecruiterEntity, Long> {
    @Query("select recruiter from RecruiterEntity recruiter join fetch recruiter.user left join fetch recruiter.company")
    List<RecruiterEntity> findAllWithUser();

    Optional<RecruiterEntity> findByUserId(Long userId);
}
