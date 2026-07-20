package com.example.ats.application.port.in;

import com.example.ats.application.dto.request.ApplicationStatusRequest;
import com.example.ats.application.dto.request.JobApplicationRequest;
import com.example.ats.application.dto.response.JobApplicationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobApplicationUseCase {
    // Candidate
    JobApplicationResponse apply(JobApplicationRequest request, Long candidateId);
    Page<JobApplicationResponse> findMyApplications(Long candidateId, Pageable pageable);
    JobApplicationResponse findMyApplicationById(Long id, Long candidateId);
    JobApplicationResponse withdraw(Long id, Long candidateId);

    // Recruiter
    Page<JobApplicationResponse> findByJobOwner(Long recruiterId, Pageable pageable);
    Page<JobApplicationResponse> findByJobAndJobOwner(Long jobId, Long recruiterId, Pageable pageable);
    JobApplicationResponse findByIdAndJobOwner(Long id, Long recruiterId);
    JobApplicationResponse updateStatusByJobOwner(Long id, ApplicationStatusRequest request, Long recruiterId);

    // Admin
    Page<JobApplicationResponse> findAll(Pageable pageable);
    JobApplicationResponse findById(Long id);
    Page<JobApplicationResponse> findByJob(Long jobId, Pageable pageable);
    Page<JobApplicationResponse> findByCandidate(Long candidateId, Pageable pageable);
    JobApplicationResponse updateStatus(Long id, ApplicationStatusRequest request);
    void delete(Long id);
}
