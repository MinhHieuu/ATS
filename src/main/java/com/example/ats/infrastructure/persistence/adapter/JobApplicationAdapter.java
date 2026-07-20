package com.example.ats.infrastructure.persistence.adapter;

import com.example.ats.application.mapper.JobApplicationMapper;
import com.example.ats.application.port.out.JobApplicationRepository;
import com.example.ats.domain.exception.ResourceNotFoundException;
import com.example.ats.domain.model.JobApplication;
import com.example.ats.infrastructure.persistence.entity.ApplicationEntity;
import com.example.ats.infrastructure.persistence.entity.CandidateEntity;
import com.example.ats.infrastructure.persistence.entity.JobEntity;
import com.example.ats.infrastructure.persistence.entity.ResumeEntity;
import com.example.ats.infrastructure.persistence.repository.SpringDataApplicationRepository;
import com.example.ats.infrastructure.persistence.repository.SpringDataCandidateRepository;
import com.example.ats.infrastructure.persistence.repository.SpringDataJobRepository;
import com.example.ats.infrastructure.persistence.repository.SpringDataResumeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class JobApplicationAdapter implements JobApplicationRepository {
    // Cac lookup mot ket qua deu nem cung mot message: "khong ton tai" va "khong phai cua ban"
    // phai khong phan biet duoc, neu khac nhau la lo quyen so huu qua noi dung loi.
    private static final String NOT_FOUND_MESSAGE = "Application not found";

    private final SpringDataApplicationRepository repository;
    private final SpringDataCandidateRepository candidateRepository;
    private final SpringDataJobRepository jobRepository;
    private final SpringDataResumeRepository resumeRepository;
    private final JobApplicationMapper mapper;

    public JobApplicationAdapter(SpringDataApplicationRepository repository,
                                 SpringDataCandidateRepository candidateRepository,
                                 SpringDataJobRepository jobRepository,
                                 SpringDataResumeRepository resumeRepository,
                                 JobApplicationMapper mapper) {
        this.repository = repository;
        this.candidateRepository = candidateRepository;
        this.jobRepository = jobRepository;
        this.resumeRepository = resumeRepository;
        this.mapper = mapper;
    }

    @Override
    public JobApplication save(JobApplication application) {
        CandidateEntity candidate = candidateRepository.findById(application.getCandidateId())
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));
        JobEntity job = jobRepository.findById(application.getJobId())
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));
        ResumeEntity resume = null;
        if (application.getResumeId() != null) {
            resume = resumeRepository.findById(application.getResumeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));
        }
        ApplicationEntity entity = new ApplicationEntity(application.getId(), candidate, job, resume,
                application.getStatus(), application.getSource(), application.getExpectedSalary(),
                application.getNote(), application.getAppliedAt(), application.getUpdatedAt());
        return mapper.toEntity(repository.save(entity));
    }

    @Override
    public JobApplication findById(Long id) {
        return mapper.toEntity(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_MESSAGE)));
    }

    @Override
    public Page<JobApplication> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toEntity);
    }

    @Override
    public Page<JobApplication> findByCandidate(Long candidateId, Pageable pageable) {
        return repository.findByCandidate_Id(candidateId, pageable).map(mapper::toEntity);
    }

    @Override
    public Page<JobApplication> findByJob(Long jobId, Pageable pageable) {
        return repository.findByJob_Id(jobId, pageable).map(mapper::toEntity);
    }

    @Override
    public Page<JobApplication> findByJobCreatedBy(Long createdBy, Pageable pageable) {
        return repository.findByJob_CreatedBy_Id(createdBy, pageable).map(mapper::toEntity);
    }

    @Override
    public Page<JobApplication> findByJobAndJobCreatedBy(Long jobId, Long createdBy, Pageable pageable) {
        return repository.findByJob_IdAndJob_CreatedBy_Id(jobId, createdBy, pageable).map(mapper::toEntity);
    }

    @Override
    public JobApplication findByIdAndJobCreatedBy(Long id, Long createdBy) {
        return mapper.toEntity(repository.findByIdAndJob_CreatedBy_Id(id, createdBy)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_MESSAGE)));
    }

    @Override
    public JobApplication findByIdAndCandidate(Long id, Long candidateId) {
        return mapper.toEntity(repository.findByIdAndCandidate_Id(id, candidateId)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_MESSAGE)));
    }

    @Override
    public boolean existsByCandidateAndJob(Long candidateId, Long jobId) {
        return repository.existsByCandidate_IdAndJob_Id(candidateId, jobId);
    }

    @Override
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(NOT_FOUND_MESSAGE);
        }
        repository.deleteById(id);
    }
}
