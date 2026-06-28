package com.example.ats.infrastructure.persistence.adapter;

import com.example.ats.application.mapper.JobApplicationMapper;
import com.example.ats.application.port.out.JobApplicationRepository;
import com.example.ats.domain.exception.ResourceNotFoundException;
import com.example.ats.domain.model.JobApplication;
import com.example.ats.infrastructure.persistence.entity.ApplicationEntity;
import com.example.ats.infrastructure.persistence.entity.ApplicationStageEntity;
import com.example.ats.infrastructure.persistence.entity.CandidateEntity;
import com.example.ats.infrastructure.persistence.entity.JobEntity;
import com.example.ats.infrastructure.persistence.repository.SpringDataApplicationRepository;
import com.example.ats.infrastructure.persistence.repository.SpringDataApplicationStageRepository;
import com.example.ats.infrastructure.persistence.repository.SpringDataCandidateRepository;
import com.example.ats.infrastructure.persistence.repository.SpringDataJobRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JobApplicationAdapter implements JobApplicationRepository {
    private final SpringDataApplicationRepository repository;
    private final SpringDataCandidateRepository candidateRepository;
    private final SpringDataJobRepository jobRepository;
    private final SpringDataApplicationStageRepository stageRepository;
    private final JobApplicationMapper mapper;

    public JobApplicationAdapter(SpringDataApplicationRepository repository,
                                 SpringDataCandidateRepository candidateRepository,
                                 SpringDataJobRepository jobRepository,
                                 SpringDataApplicationStageRepository stageRepository,
                                 JobApplicationMapper mapper) {
        this.repository = repository;
        this.candidateRepository = candidateRepository;
        this.jobRepository = jobRepository;
        this.stageRepository = stageRepository;
        this.mapper = mapper;
    }

    @Override
    public JobApplication save(JobApplication application) {
        CandidateEntity candidate = candidateRepository.findById(application.getCandidateId())
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));
        JobEntity job = jobRepository.findById(application.getJobId())
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));
        ApplicationStageEntity stage = application.getStageId() == null ? null : stageRepository.findById(application.getStageId())
                .orElseThrow(() -> new ResourceNotFoundException("Application stage not found"));
        ApplicationEntity entity = new ApplicationEntity(application.getId(), candidate, job, stage,
                application.getStatus(), application.getExpectedSalary(), application.getNote(),
                application.getAppliedAt(), application.getUpdatedAt(), null);
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public JobApplication findById(Long id) {
        return mapper.toDomain(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found")));
    }

    @Override
    public List<JobApplication> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<JobApplication> findByCandidate(Long candidateId) {
        return repository.findByCandidate_IdOrderByAppliedAtDesc(candidateId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<JobApplication> findByJob(Long jobId) {
        return repository.findByJob_IdOrderByAppliedAtDesc(jobId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public boolean existsByCandidateAndJob(Long candidateId, Long jobId) {
        return repository.existsByCandidate_IdAndJob_Id(candidateId, jobId);
    }

    @Override
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Application not found");
        }
        repository.deleteById(id);
    }
}
