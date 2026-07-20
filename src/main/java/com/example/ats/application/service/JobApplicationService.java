package com.example.ats.application.service;

import com.example.ats.application.dto.request.ApplicationStatusRequest;
import com.example.ats.application.dto.request.JobApplicationRequest;
import com.example.ats.application.dto.response.JobApplicationResponse;
import com.example.ats.application.mapper.JobApplicationMapper;
import com.example.ats.application.port.in.JobApplicationUseCase;
import com.example.ats.application.port.out.JobApplicationRepository;
import com.example.ats.application.port.out.JobRepository;
import com.example.ats.application.port.out.ResumeRepository;
import com.example.ats.domain.exception.BusinessRuleException;
import com.example.ats.domain.exception.ResourceNotFoundException;
import com.example.ats.domain.model.ApplicationStatus;
import com.example.ats.domain.model.Job;
import com.example.ats.domain.model.JobApplication;
import com.example.ats.domain.model.JobStatus;
import com.example.ats.domain.model.Resume;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
public class JobApplicationService implements JobApplicationUseCase {
    private static final ApplicationStatus INITIAL_STATUS = ApplicationStatus.APPLICATION_CREATED;
    private static final Map<ApplicationStatus, Set<ApplicationStatus>> ALLOWED_STATUS_TRANSITIONS = Map.of(
            ApplicationStatus.APPLICATION_CREATED,
            Set.of(ApplicationStatus.SCREENING, ApplicationStatus.REJECTED, ApplicationStatus.WITHDRAWN),
            ApplicationStatus.SCREENING,
            Set.of(ApplicationStatus.INTERVIEW, ApplicationStatus.REJECTED, ApplicationStatus.WITHDRAWN),
            ApplicationStatus.INTERVIEW,
            Set.of(ApplicationStatus.OFFER, ApplicationStatus.REJECTED, ApplicationStatus.WITHDRAWN),
            ApplicationStatus.OFFER,
            Set.of(ApplicationStatus.HIRED, ApplicationStatus.REJECTED, ApplicationStatus.WITHDRAWN),
            ApplicationStatus.HIRED, Set.of(),
            ApplicationStatus.REJECTED, Set.of(),
            ApplicationStatus.WITHDRAWN, Set.of()
    );

    private final JobApplicationRepository repository;
    private final JobRepository jobRepository;
    private final ResumeRepository resumeRepository;
    private final JobApplicationMapper mapper;

    public JobApplicationService(JobApplicationRepository repository, JobRepository jobRepository,
                                 ResumeRepository resumeRepository, JobApplicationMapper mapper) {
        this.repository = repository;
        this.jobRepository = jobRepository;
        this.resumeRepository = resumeRepository;
        this.mapper = mapper;
    }

    @Override
    public JobApplicationResponse apply(JobApplicationRequest request, Long candidateId) {
        if (repository.existsByCandidateAndJob(candidateId, request.getJobId())) {
            throw new BusinessRuleException("Candidate has already applied to this job");
        }
        Job job = jobRepository.findById(request.getJobId()).job();
        if (job.getStatus() != JobStatus.OPEN) {
            throw new BusinessRuleException("Candidate can only apply to an open job");
        }
        validateResumeOwnership(request.getResumeId(), candidateId);
        JobApplication application = new JobApplication(null, candidateId, request.getJobId(), request.getResumeId(),
                INITIAL_STATUS, request.getSource(), request.getExpectedSalary(), request.getNote(),
                Instant.now(), null);
        return mapper.toResponse(repository.save(application));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobApplicationResponse> findMyApplications(Long candidateId, Pageable pageable) {
        return repository.findByCandidate(candidateId, pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public JobApplicationResponse findMyApplicationById(Long id, Long candidateId) {
        return mapper.toResponse(repository.findByIdAndCandidate(id, candidateId));
    }

    @Override
    public JobApplicationResponse withdraw(Long id, Long candidateId) {
        JobApplication application = repository.findByIdAndCandidate(id, candidateId);
        return applyStatus(application, ApplicationStatus.WITHDRAWN);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobApplicationResponse> findByJobOwner(Long recruiterId, Pageable pageable) {
        return repository.findByJobCreatedBy(recruiterId, pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobApplicationResponse> findByJobAndJobOwner(Long jobId, Long recruiterId, Pageable pageable) {
        return repository.findByJobAndJobCreatedBy(jobId, recruiterId, pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public JobApplicationResponse findByIdAndJobOwner(Long id, Long recruiterId) {
        return mapper.toResponse(repository.findByIdAndJobCreatedBy(id, recruiterId));
    }

    @Override
    public JobApplicationResponse updateStatusByJobOwner(Long id, ApplicationStatusRequest request, Long recruiterId) {
        rejectWithdrawnTarget(request.getStatus());
        JobApplication application = repository.findByIdAndJobCreatedBy(id, recruiterId);
        return applyStatus(application, request.getStatus());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobApplicationResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public JobApplicationResponse findById(Long id) {
        return mapper.toResponse(repository.findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobApplicationResponse> findByJob(Long jobId, Pageable pageable) {
        return repository.findByJob(jobId, pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobApplicationResponse> findByCandidate(Long candidateId, Pageable pageable) {
        return repository.findByCandidate(candidateId, pageable).map(mapper::toResponse);
    }

    @Override
    public JobApplicationResponse updateStatus(Long id, ApplicationStatusRequest request) {
        rejectWithdrawnTarget(request.getStatus());
        return applyStatus(repository.findById(id), request.getStatus());
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private JobApplicationResponse applyStatus(JobApplication application, ApplicationStatus newStatus) {
        validateStatusTransition(application.getStatus(), newStatus);
        application.setStatus(newStatus);
        application.setUpdatedAt(Instant.now());
        return mapper.toResponse(repository.save(application));
    }

    // Chi ung vien duoc rut don. Neu khong chan o day thi recruiter/admin co the
    // rut don thay ung vien vi WITHDRAWN nam trong bang chuyen trang thai.
    private void rejectWithdrawnTarget(ApplicationStatus newStatus) {
        if (newStatus == ApplicationStatus.WITHDRAWN) {
            throw new BusinessRuleException("Only the candidate can withdraw an application");
        }
    }

    // Tra 404 thay vi 409 khi CV thuoc ve nguoi khac de khong xac nhan CV do ton tai.
    private void validateResumeOwnership(Long resumeId, Long candidateId) {
        if (resumeId == null) {
            return;
        }
        Resume resume = resumeRepository.findById(resumeId);
        if (!resume.getCandidateId().equals(candidateId)) {
            throw new ResourceNotFoundException("Resume not found");
        }
    }

    private void validateStatusTransition(ApplicationStatus currentStatus, ApplicationStatus newStatus) {
        if (currentStatus == newStatus) {
            return;
        }
        Set<ApplicationStatus> allowedNextStatuses = ALLOWED_STATUS_TRANSITIONS.getOrDefault(currentStatus, Set.of());
        if (!allowedNextStatuses.contains(newStatus)) {
            throw new BusinessRuleException("Invalid application status transition from "
                    + currentStatus + " to " + newStatus);
        }
    }
}
