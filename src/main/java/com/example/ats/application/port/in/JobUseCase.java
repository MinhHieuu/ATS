package com.example.ats.application.port.in;

import com.example.ats.application.dto.request.JobRequest;
import com.example.ats.application.dto.response.JobResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobUseCase {
    JobResponse create(JobRequest request);
    JobResponse update(Long id, JobRequest request);
    JobResponse activate(Long id);
    JobResponse deactivate(Long id);
    JobResponse findById(Long id);
    JobResponse findByIdNotClosed(Long id);
    Page<JobResponse> findAll(Pageable pageable);
    Page<JobResponse> findAllNotClosed(Pageable pageable);
    Page<JobResponse> findByCreatedBy(Long createdBy, Pageable pageable);
    Page<JobResponse> searchByTitle(String title, Pageable pageable);
    Page<JobResponse> searchByTitleNotClosed(String title, Pageable pageable);
    Page<JobResponse> searchByTitleAndCreatedBy(String title, Long createdBy, Pageable pageable);
    void delete(Long id);
}
