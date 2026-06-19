package com.example.ats.infrastructure.persistence.repository;

import com.example.ats.infrastructure.persistence.entity.JobEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SpringDataJobRepository extends JpaRepository<JobEntity, Long> {
}
