package com.example.ats.application.port.out;

import com.example.ats.domain.model.InterviewResult;
import com.example.ats.domain.view.InterviewView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InterviewRepository {
    InterviewView save(Long applicationId, com.example.ats.domain.model.Interview interview);
    Page<InterviewView> findAll(Pageable pageable);
    InterviewView findById(Long id);
    Page<InterviewView> findByApplication(Long applicationId, Pageable pageable);
    Page<InterviewView> findByJobCreatedBy(Long recruiterId, Pageable pageable);
    InterviewView findByIdAndJobCreatedBy(Long id, Long recruiterId);
    Page<InterviewView> findByApplicationAndJobCreatedBy(Long applicationId, Long recruiterId, Pageable pageable);
    Page<InterviewView> findByCandidate(Long candidateId, Pageable pageable);
    InterviewView findByIdAndCandidate(Long id, Long candidateId);
    boolean existsByApplicationAndResult(Long applicationId, InterviewResult result);
    void deleteById(Long id);
}
