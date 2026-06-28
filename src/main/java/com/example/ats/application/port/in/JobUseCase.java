package com.example.ats.application.port.in;

import com.example.ats.application.dto.request.JobRequest;
import com.example.ats.application.dto.response.JobResponse;

import java.util.List;

public interface JobUseCase {
    JobResponse create(JobRequest request);
    JobResponse update(Long id, JobRequest request);
    JobResponse findById(Long id);
    List<JobResponse> findAll();
    void delete(Long id);
}
