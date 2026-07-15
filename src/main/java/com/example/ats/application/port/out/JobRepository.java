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
    void deleteById(Long id);
}
