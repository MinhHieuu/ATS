package com.example.ats.application.port.out;

import com.example.ats.domain.model.Job;

import java.util.List;

public interface JobRepository {
    Job save(Job job);
    Job findById(Long id);
    List<Job> findAll();
    void deleteById(Long id);
}
