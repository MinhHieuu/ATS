package com.example.ats.application.port.in;

import com.example.ats.application.dto.request.ApplicationStatusRequest;
import com.example.ats.application.dto.request.JobApplicationRequest;
import com.example.ats.application.dto.response.JobApplicationResponse;

import java.util.List;

public interface JobApplicationUseCase {
    JobApplicationResponse create(JobApplicationRequest request);
    JobApplicationResponse update(Long id, JobApplicationRequest request);
    JobApplicationResponse changeStatus(Long id, ApplicationStatusRequest request);
    JobApplicationResponse findById(Long id);
    List<JobApplicationResponse> findAll();
    List<JobApplicationResponse> findByCandidate(Long candidateId);
    List<JobApplicationResponse> findByJob(Long jobId);
    void delete(Long id);
}
