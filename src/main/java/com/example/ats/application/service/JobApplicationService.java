package com.example.ats.application.service;

import com.example.ats.application.dto.request.ApplicationStatusRequest;
import com.example.ats.application.dto.request.JobApplicationRequest;
import com.example.ats.application.dto.response.JobApplicationResponse;
import com.example.ats.application.mapper.JobApplicationMapper;
import com.example.ats.application.port.in.JobApplicationUseCase;
import com.example.ats.application.port.out.JobApplicationRepository;
import com.example.ats.domain.exception.BusinessRuleException;
import com.example.ats.domain.model.ApplicationStatus;
import com.example.ats.domain.model.JobApplication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class JobApplicationService implements JobApplicationUseCase {
    private final JobApplicationRepository repository;
    private final JobApplicationMapper mapper;

    public JobApplicationService(JobApplicationRepository repository, JobApplicationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public JobApplicationResponse create(JobApplicationRequest request) {
        if (repository.existsByCandidateAndJob(request.getCandidateId(), request.getJobId())) {
            throw new BusinessRuleException("Candidate has already applied to this job");
        }
        Instant now = Instant.now();
        JobApplication application = new JobApplication(null, request.getCandidateId(), request.getJobId(),
                request.getStageId(), request.getStatus() == null ? ApplicationStatus.APPLIED : request.getStatus(),
                request.getSource(), request.getExpectedSalary(), request.getNote(), now, null);
        return mapper.toResponse(repository.save(application));
    }

    @Override
    public JobApplicationResponse update(Long id, JobApplicationRequest request) {
        JobApplication application = repository.findById(id);
        application.setCandidateId(request.getCandidateId());
        application.setJobId(request.getJobId());
        application.setStageId(request.getStageId());
        application.setStatus(request.getStatus() == null ? application.getStatus() : request.getStatus());
        application.setSource(request.getSource());
        application.setExpectedSalary(request.getExpectedSalary());
        application.setNote(request.getNote());
        application.setUpdatedAt(Instant.now());
        return mapper.toResponse(repository.save(application));
    }

    @Override
    public JobApplicationResponse changeStatus(Long id, ApplicationStatusRequest request) {
        JobApplication application = repository.findById(id).changeStatus(request.getStatus(), Instant.now());
        application.setStageId(request.getStageId());
        return mapper.toResponse(repository.save(application));
    }

    @Override
    @Transactional(readOnly = true)
    public JobApplicationResponse findById(Long id) {
        return mapper.toResponse(repository.findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobApplicationResponse> findAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobApplicationResponse> findByCandidate(Long candidateId) {
        return repository.findByCandidate(candidateId).stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobApplicationResponse> findByJob(Long jobId) {
        return repository.findByJob(jobId).stream().map(mapper::toResponse).toList();
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
