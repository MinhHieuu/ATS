package com.example.ats.application.port.out;

import com.example.ats.domain.model.Job;
import com.example.ats.domain.model.JobStatus;
import com.example.ats.domain.view.JobView;

import java.util.List;

public interface JobRepository {
    JobView save(Job job);
    JobView findById(Long id);
    List<JobView> findAll();
    List<JobView> finByStatus(JobStatus status);
    List<JobView> findByStatusNot(JobStatus status);
    List<JobView> findByCreatedBy(Long createdBy);
    List<JobView> searchByTitle(String title);
    List<JobView> searchByTitleAndStatusNot(String title, JobStatus status);
    List<JobView> searchByTitleAndCreatedBy(String title, Long createdBy);
    void deleteById(Long id);
}
