package com.example.ats.infrastructure.persistence.repository;

import com.example.ats.infrastructure.persistence.entity.InterviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SpringDataInterviewRepository extends JpaRepository<InterviewEntity, Long> {
    List<InterviewEntity> findByApplication_Id(Long applicationId);
}
