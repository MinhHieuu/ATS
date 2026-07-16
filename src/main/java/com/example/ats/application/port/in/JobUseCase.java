package com.example.ats.application.port.in;

import com.example.ats.application.dto.request.JobRequest;
import com.example.ats.application.dto.response.JobResponse;

import java.util.List;

public interface JobUseCase {
    JobResponse create(JobRequest request);
    JobResponse update(Long id, JobRequest request);
    JobResponse activate(Long id);
    JobResponse deactivate(Long id);
    JobResponse findById(Long id);
    JobResponse findByIdNotClosed(Long id);
    List<JobResponse> findAll();
    List<JobResponse> findAllNotClosed();
    List<JobResponse> findByCreatedBy(Long createdBy);
    List<JobResponse> searchByTitle(String title);
    List<JobResponse> searchByTitleNotClosed(String title);
    List<JobResponse> searchByTitleAndCreatedBy(String title, Long createdBy);
    void delete(Long id);
}
