package com.example.ats.application.port.out;

import com.example.ats.domain.model.JobApplication;

import java.util.List;

public interface JobApplicationRepository {
    JobApplication save(JobApplication application);
    JobApplication findById(Long id);
    List<JobApplication> findAll();
    List<JobApplication> findByCandidate(Long candidateId);
    List<JobApplication> findByJob(Long jobId);
    boolean existsByCandidateAndJob(Long candidateId, Long jobId);
    void deleteById(Long id);
}
