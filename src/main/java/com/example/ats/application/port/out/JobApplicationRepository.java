package com.example.ats.application.port.out;

import com.example.ats.domain.model.JobApplication;
import com.example.ats.domain.view.JobApplicationView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobApplicationRepository {
    JobApplicationView save(JobApplication application);
    JobApplicationView findById(Long id);
    Page<JobApplicationView> findAll(Pageable pageable);
    Page<JobApplicationView> findByCandidate(Long candidateId, Pageable pageable);
    Page<JobApplicationView> findByJob(Long jobId, Pageable pageable);
    Page<JobApplicationView> findByJobCreatedBy(Long createdBy, Pageable pageable);
    Page<JobApplicationView> findByJobAndJobCreatedBy(Long jobId, Long createdBy, Pageable pageable);
    JobApplicationView findByIdAndJobCreatedBy(Long id, Long createdBy);
    JobApplicationView findByIdAndCandidate(Long id, Long candidateId);
    boolean existsByCandidateAndJob(Long candidateId, Long jobId);
    void deleteById(Long id);
}
