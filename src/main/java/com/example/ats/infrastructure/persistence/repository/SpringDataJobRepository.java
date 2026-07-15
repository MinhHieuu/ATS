package com.example.ats.infrastructure.persistence.repository;

import com.example.ats.domain.model.JobStatus;
import com.example.ats.infrastructure.persistence.entity.JobEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataJobRepository extends JpaRepository<JobEntity, Long> {
    List<JobEntity> findByStatus(JobStatus status);
}
