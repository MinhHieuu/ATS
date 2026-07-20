package com.example.ats.application.port.out;

import com.example.ats.domain.model.JobApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobApplicationRepository {
    JobApplication save(JobApplication application);
    JobApplication findById(Long id);
    Page<JobApplication> findAll(Pageable pageable);
    Page<JobApplication> findByCandidate(Long candidateId, Pageable pageable);
    Page<JobApplication> findByJob(Long jobId, Pageable pageable);
    Page<JobApplication> findByJobCreatedBy(Long createdBy, Pageable pageable);
    Page<JobApplication> findByJobAndJobCreatedBy(Long jobId, Long createdBy, Pageable pageable);
    JobApplication findByIdAndJobCreatedBy(Long id, Long createdBy);
    JobApplication findByIdAndCandidate(Long id, Long candidateId);
    boolean existsByCandidateAndJob(Long candidateId, Long jobId);
    void deleteById(Long id);
}
