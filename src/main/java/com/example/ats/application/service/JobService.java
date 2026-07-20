package com.example.ats.application.service;

import com.example.ats.application.dto.request.JobRequest;
import com.example.ats.application.dto.response.JobResponse;
import com.example.ats.application.mapper.CompanyMapper;
import com.example.ats.application.mapper.JobMapper;
import com.example.ats.application.port.in.JobUseCase;
import com.example.ats.application.port.out.JobRepository;
import com.example.ats.domain.exception.ResourceNotFoundException;
import com.example.ats.domain.model.Job;
import com.example.ats.domain.model.JobStatus;
import com.example.ats.domain.view.JobView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Transactional
public class JobService implements JobUseCase {
    private final JobRepository repository;
    private final JobMapper mapper;
    private final CompanyMapper companyMapper;

    public JobService(JobRepository repository, JobMapper mapper, CompanyMapper companyMapper) {
        this.repository = repository;
        this.mapper = mapper;
        this.companyMapper = companyMapper;
    }

    @Override
    public JobResponse create(JobRequest request) {
        Instant now = Instant.now();
        JobStatus status = request.getStatus() != null ? request.getStatus() : JobStatus.OPEN;
        Job job = new Job(null, request.getTitle(), request.getDescription(), request.getRequirements(),
                request.getLocation(), request.getEmploymentType(), request.getCompanyId(),
                request.getSalaryMin(), request.getSalaryMax(), status, request.getCreatedBy(), now, null);
        return toResponse(repository.save(job));
    }

    @Override
    public JobResponse update(Long id, JobRequest request) {
        Job job = repository.findById(id).job();
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setRequirements(request.getRequirements());
        job.setLocation(request.getLocation());
        job.setEmploymentType(request.getEmploymentType());
        job.setCompanyId(request.getCompanyId());
        job.setSalaryMin(request.getSalaryMin());
        job.setSalaryMax(request.getSalaryMax());
        if (request.getStatus() != null) {
            job.setStatus(request.getStatus());
        }
        job.setUpdatedAt(Instant.now());
        return toResponse(repository.save(job));
    }

    @Override
    public JobResponse activate(Long id) {
        return updateStatus(id, JobStatus.OPEN);
    }

    @Override
    public JobResponse deactivate(Long id) {
        return updateStatus(id, JobStatus.CLOSED);
    }

    @Override
    public JobResponse findById(Long id) {
        return toResponse(repository.findById(id));
    }

    @Override
    public JobResponse findByIdNotClosed(Long id) {
        JobView view = repository.findById(id);
        // Bao 404 thay vi 403 de khong lo ra su ton tai cua job da dong.
        if (view.job().getStatus() == JobStatus.CLOSED) {
            throw new ResourceNotFoundException("Job not found");
        }
        return toResponse(view);
    }

    @Override
    public Page<JobResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponse);
    }

    @Override
    public Page<JobResponse> findAllNotClosed(Pageable pageable) {
        return repository.findByStatusNot(JobStatus.CLOSED, pageable).map(this::toResponse);
    }

    @Override
    public Page<JobResponse> findByCreatedBy(Long createdBy, Pageable pageable) {
        return repository.findByCreatedBy(createdBy, pageable).map(this::toResponse);
    }

    @Override
    public Page<JobResponse> searchByTitle(String title, Pageable pageable) {
        return repository.searchByTitle(title, pageable).map(this::toResponse);
    }

    @Override
    public Page<JobResponse> searchByTitleNotClosed(String title, Pageable pageable) {
        return repository.searchByTitleAndStatusNot(title, JobStatus.CLOSED, pageable).map(this::toResponse);
    }

    @Override
    public Page<JobResponse> searchByTitleAndCreatedBy(String title, Long createdBy, Pageable pageable) {
        return repository.searchByTitleAndCreatedBy(title, createdBy, pageable).map(this::toResponse);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private JobResponse updateStatus(Long id, JobStatus status) {
        Job job = repository.findById(id).job();
        job.setStatus(status);
        job.setUpdatedAt(Instant.now());
        return toResponse(repository.save(job));
    }

    private JobResponse toResponse(JobView view) {
        JobResponse response = mapper.toResponse(view.job());
        if (view.company() != null) {
            response.setCompany(companyMapper.toResponse(view.company()));
        }
        return response;
    }
}
