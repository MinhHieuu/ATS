package com.example.ats.application.port.out;

import com.example.ats.domain.model.Job;
import com.example.ats.domain.model.JobStatus;
import com.example.ats.domain.view.JobView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobRepository {
    JobView save(Job job);
    JobView findById(Long id);
    Page<JobView> findAll(Pageable pageable);
    Page<JobView> finByStatus(JobStatus status, Pageable pageable);
    Page<JobView> findByStatusNot(JobStatus status, Pageable pageable);
    Page<JobView> findByCreatedBy(Long createdBy, Pageable pageable);
    Page<JobView> searchByTitle(String title, Pageable pageable);
    Page<JobView> searchByTitleAndStatusNot(String title, JobStatus status, Pageable pageable);
    Page<JobView> searchByTitleAndCreatedBy(String title, Long createdBy, Pageable pageable);
    Page<JobView> findByCategory(Long categoryId, Pageable pageable);
    Page<JobView> findByCategoryAndStatusNot(Long categoryId, JobStatus status, Pageable pageable);
    Page<JobView> findByCategoryAndCreatedBy(Long categoryId, Long createdBy, Pageable pageable);
    void deleteById(Long id);
}
