package com.example.ats.application.port.out;

import com.example.ats.domain.model.Interview;

import java.util.List;

public interface InterviewRepository {
    Interview save(Interview interview);
    Interview findById(Long id);
    List<Interview> findAll();
    List<Interview> findByApplication(Long applicationId);
    void deleteById(Long id);
}
