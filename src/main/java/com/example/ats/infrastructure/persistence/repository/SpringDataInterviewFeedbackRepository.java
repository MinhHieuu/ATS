package com.example.ats.infrastructure.persistence.repository;

import com.example.ats.infrastructure.persistence.entity.InterviewFeedbackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SpringDataInterviewFeedbackRepository
        extends JpaRepository<InterviewFeedbackEntity, Long> {
    List<InterviewFeedbackEntity> findByInterview_Id(Long interviewId);
}
