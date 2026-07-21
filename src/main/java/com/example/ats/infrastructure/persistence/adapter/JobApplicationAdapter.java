package com.example.ats.infrastructure.persistence.adapter;

import com.example.ats.application.mapper.CandidateMapper;
import com.example.ats.application.mapper.CompanyMapper;
import com.example.ats.application.mapper.JobApplicationMapper;
import com.example.ats.application.mapper.JobMapper;
import com.example.ats.application.mapper.ResumeMapper;
import com.example.ats.application.mapper.UserMapper;
import com.example.ats.application.port.out.JobApplicationRepository;
import com.example.ats.domain.exception.ResourceNotFoundException;
import com.example.ats.domain.model.Candidate;
import com.example.ats.domain.model.Company;
import com.example.ats.domain.model.Job;
import com.example.ats.domain.model.JobApplication;
import com.example.ats.domain.model.Resume;
import com.example.ats.domain.model.User;
import com.example.ats.domain.view.JobApplicationView;
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
    private static final String NOT_FOUND_MESSAGE = "Application not found";

    private final SpringDataApplicationRepository repository;
    private final SpringDataCandidateRepository candidateRepository;
    private final SpringDataJobRepository jobRepository;
    private final SpringDataResumeRepository resumeRepository;
    private final JobApplicationMapper mapper;
    private final CandidateMapper candidateMapper;
    private final UserMapper userMapper;
    private final JobMapper jobMapper;
    private final CompanyMapper companyMapper;
    private final ResumeMapper resumeMapper;

    public JobApplicationAdapter(SpringDataApplicationRepository repository,
                                 SpringDataCandidateRepository candidateRepository,
                                 SpringDataJobRepository jobRepository,
                                 SpringDataResumeRepository resumeRepository,
                                 JobApplicationMapper mapper,
                                 CandidateMapper candidateMapper,
                                 UserMapper userMapper,
                                 JobMapper jobMapper,
                                 CompanyMapper companyMapper,
                                 ResumeMapper resumeMapper) {
        this.repository = repository;
        this.candidateRepository = candidateRepository;
        this.jobRepository = jobRepository;
        this.resumeRepository = resumeRepository;
        this.mapper = mapper;
        this.candidateMapper = candidateMapper;
        this.userMapper = userMapper;
        this.jobMapper = jobMapper;
        this.companyMapper = companyMapper;
        this.resumeMapper = resumeMapper;
    }

    @Override
    public JobApplicationView save(JobApplication application) {
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
        return toView(repository.save(entity));
    }

    @Override
    public JobApplicationView findById(Long id) {
        return toView(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_MESSAGE)));
    }

    @Override
    public Page<JobApplicationView> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toView);
    }

    @Override
    public Page<JobApplicationView> findByCandidate(Long candidateId, Pageable pageable) {
        return repository.findByCandidate_Id(candidateId, pageable).map(this::toView);
    }

    @Override
    public Page<JobApplicationView> findByJob(Long jobId, Pageable pageable) {
        return repository.findByJob_Id(jobId, pageable).map(this::toView);
    }

    @Override
    public Page<JobApplicationView> findByJobCreatedBy(Long createdBy, Pageable pageable) {
        return repository.findByJob_CreatedBy_Id(createdBy, pageable).map(this::toView);
    }

    @Override
    public Page<JobApplicationView> findByJobAndJobCreatedBy(Long jobId, Long createdBy, Pageable pageable) {
        return repository.findByJob_IdAndJob_CreatedBy_Id(jobId, createdBy, pageable).map(this::toView);
    }

    @Override
    public JobApplicationView findByIdAndJobCreatedBy(Long id, Long createdBy) {
        return toView(repository.findByIdAndJob_CreatedBy_Id(id, createdBy)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_MESSAGE)));
    }

    @Override
    public JobApplicationView findByIdAndCandidate(Long id, Long candidateId) {
        return toView(repository.findByIdAndCandidate_Id(id, candidateId)
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

    private JobApplicationView toView(ApplicationEntity entity) {
        JobApplication application = mapper.toEntity(entity);

        Candidate candidate = candidateMapper.toEntity(entity.getCandidate());
        candidate.setUserId(entity.getCandidate().getUser().getId());
        User candidateUser = userMapper.toEntity(entity.getCandidate().getUser());

        Job job = jobMapper.toEntity(entity.getJob());
        job.setCompanyId(entity.getJob().getCompany().getId());
        Company jobCompany = companyMapper.toEntity(entity.getJob().getCompany());

        User jobCreatedBy = null;
        if (entity.getJob().getCreatedBy() != null) {
            job.setCreatedBy(entity.getJob().getCreatedBy().getId());
            jobCreatedBy = userMapper.toEntity(entity.getJob().getCreatedBy());
        }

        Resume resume = entity.getResume() != null ? resumeMapper.toDomain(entity.getResume()) : null;

        return new JobApplicationView(application, candidate, candidateUser, job, jobCompany, jobCreatedBy, resume);
    }
}
