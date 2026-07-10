package com.example.ats.application.service;

import com.example.ats.application.dto.request.JobRequest;
import com.example.ats.application.dto.response.JobResponse;
import com.example.ats.application.mapper.JobMapper;
import com.example.ats.application.port.in.JobUseCase;
import com.example.ats.application.port.out.JobRepository;
import com.example.ats.domain.model.ActivityAction;
import com.example.ats.domain.model.EntityType;
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
    private final ActivityLogRecorder activityLogRecorder;

    public JobService(JobRepository repository, JobMapper mapper, ActivityLogRecorder activityLogRecorder) {
        this.repository = repository;
        this.mapper = mapper;
        this.activityLogRecorder = activityLogRecorder;
    }

    @Override
    public JobResponse create(JobRequest request) {
        validateSalaryRange(request);
        Instant now = Instant.now();
        Job job = new Job(null, request.getTitle(), request.getDescription(), request.getRequirements(),
                request.getLocation(), request.getEmploymentType(), request.getSalaryMin(), request.getSalaryMax(),
                request.getStatus() == null ? JobStatus.OPEN : request.getStatus(), request.getCreatedBy(), now, null);
        Job savedJob = repository.save(job);
        activityLogRecorder.record(ActivityAction.CREATE, EntityType.JOB, savedJob.getId(),
                "Tạo job: " + savedJob.getTitle());
        return mapper.toResponse(savedJob);
    }

    @Override
    public JobResponse update(Long id, JobRequest request) {
        validateSalaryRange(request);
        Job job = repository.findById(id);
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setRequirements(request.getRequirements());
        job.setLocation(request.getLocation());
        job.setEmploymentType(request.getEmploymentType());
        job.setSalaryMin(request.getSalaryMin());
        job.setSalaryMax(request.getSalaryMax());
        job.setStatus(request.getStatus() == null ? job.getStatus() : request.getStatus());
        job.setUpdatedAt(Instant.now());
        Job savedJob = repository.save(job);
        activityLogRecorder.record(ActivityAction.UPDATE, EntityType.JOB, savedJob.getId(),
                "Cập nhật job: " + savedJob.getTitle());
        return mapper.toResponse(savedJob);
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
        Job job = repository.findById(id);
        repository.deleteById(id);
        activityLogRecorder.record(ActivityAction.DELETE, EntityType.JOB, id,
                "Xóa job: " + job.getTitle());
    }

    private void validateSalaryRange(JobRequest request) {
        if (request.getSalaryMin() != null && request.getSalaryMax() != null
                && request.getSalaryMin().compareTo(request.getSalaryMax()) > 0) {
            throw new IllegalArgumentException("Minimum salary must be less than or equal to maximum salary");
        }
    }
}
