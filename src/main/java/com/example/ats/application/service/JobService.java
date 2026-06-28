package com.example.ats.application.service;

import com.example.ats.application.dto.request.JobRequest;
import com.example.ats.application.dto.response.JobResponse;
import com.example.ats.application.mapper.JobMapper;
import com.example.ats.application.port.in.JobUseCase;
import com.example.ats.application.port.out.JobRepository;
import com.example.ats.domain.model.Job;
import com.example.ats.domain.model.JobStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class JobService implements JobUseCase {
    private final JobRepository repository;
    private final JobMapper mapper;

    public JobService(JobRepository repository, JobMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public JobResponse create(JobRequest request) {
        Instant now = Instant.now();
        Job job = new Job(null, request.getTitle(), request.getDescription(), request.getRequirements(),
                request.getLocation(), request.getEmploymentType(), request.getSalaryMin(), request.getSalaryMax(),
                request.getStatus() == null ? JobStatus.OPEN : request.getStatus(), request.getCreatedBy(), now, null);
        return mapper.toResponse(repository.save(job));
    }

    @Override
    public JobResponse update(Long id, JobRequest request) {
        Job job = repository.findById(id);
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setRequirements(request.getRequirements());
        job.setLocation(request.getLocation());
        job.setEmploymentType(request.getEmploymentType());
        job.setSalaryMin(request.getSalaryMin());
        job.setSalaryMax(request.getSalaryMax());
        job.setStatus(request.getStatus() == null ? job.getStatus() : request.getStatus());
        job.setCreatedBy(request.getCreatedBy());
        job.setUpdatedAt(Instant.now());
        return mapper.toResponse(repository.save(job));
    }

    @Override
    @Transactional(readOnly = true)
    public JobResponse findById(Long id) {
        return mapper.toResponse(repository.findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobResponse> findAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
