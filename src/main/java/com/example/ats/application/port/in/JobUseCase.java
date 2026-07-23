package com.example.ats.application.port.in;

import com.example.ats.application.dto.request.JobRequest;
import com.example.ats.application.dto.response.JobResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobUseCase {
    // Admin: dung duoc moi category, ke ca category da ngung dung.
    JobResponse create(JobRequest request);
    JobResponse update(Long id, JobRequest request);
    // Recruiter: chi duoc chon category dang active.
    JobResponse createByRecruiter(JobRequest request);
    JobResponse updateByRecruiter(Long id, JobRequest request);
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
    Page<JobResponse> findByCategory(Long categoryId, Pageable pageable);
    Page<JobResponse> findByCategoryNotClosed(Long categoryId, Pageable pageable);
    Page<JobResponse> findByCategoryAndCreatedBy(Long categoryId, Long createdBy, Pageable pageable);
    void delete(Long id);
}
