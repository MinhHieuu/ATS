package com.example.ats.application.service;

import com.example.ats.application.dto.request.ApplicationStatusRequest;
import com.example.ats.application.dto.request.JobApplicationRequest;
import com.example.ats.application.dto.response.JobApplicationResponse;
import com.example.ats.application.mapper.JobApplicationMapper;
import com.example.ats.application.port.in.JobApplicationUseCase;
import com.example.ats.application.port.in.NotificationUseCase;
import com.example.ats.application.port.out.CandidateRepository;
import com.example.ats.application.port.out.JobRepository;
import com.example.ats.application.port.out.JobApplicationRepository;
import com.example.ats.domain.exception.BusinessRuleException;
import com.example.ats.domain.model.ActivityAction;
import com.example.ats.domain.model.ApplicationStatus;
import com.example.ats.domain.model.Candidate;
import com.example.ats.domain.model.EntityType;
import com.example.ats.domain.model.Job;
import com.example.ats.domain.model.JobApplication;
import com.example.ats.domain.model.JobStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
public class JobApplicationService implements JobApplicationUseCase {
    private static final ApplicationStatus INITIAL_STATUS = ApplicationStatus.APPLICATION_CREATED;
    private static final Map<ApplicationStatus, Set<ApplicationStatus>> ALLOWED_STATUS_TRANSITIONS = Map.of(
            ApplicationStatus.APPLICATION_CREATED, Set.of(ApplicationStatus.SCREENING, ApplicationStatus.REJECTED),
            ApplicationStatus.APPLIED, Set.of(ApplicationStatus.SCREENING, ApplicationStatus.REJECTED),
            ApplicationStatus.SCREENING, Set.of(ApplicationStatus.INTERVIEW, ApplicationStatus.REJECTED),
            ApplicationStatus.INTERVIEW, Set.of(ApplicationStatus.OFFER, ApplicationStatus.OFFERED, ApplicationStatus.REJECTED),
            ApplicationStatus.OFFER, Set.of(ApplicationStatus.HIRED, ApplicationStatus.REJECTED),
            ApplicationStatus.OFFERED, Set.of(ApplicationStatus.HIRED, ApplicationStatus.REJECTED),
            ApplicationStatus.WITHDRAWN, Set.of()
    );

    private final JobApplicationRepository repository;
    private final JobRepository jobRepository;
    private final CandidateRepository candidateRepository;
    private final NotificationUseCase notificationUseCase;
    private final ActivityLogRecorder activityLogRecorder;
    private final JobApplicationMapper mapper;

    public JobApplicationService(JobApplicationRepository repository, JobRepository jobRepository,
                                 CandidateRepository candidateRepository,
                                 NotificationUseCase notificationUseCase,
                                 ActivityLogRecorder activityLogRecorder,
                                 JobApplicationMapper mapper) {
        this.repository = repository;
        this.jobRepository = jobRepository;
        this.candidateRepository = candidateRepository;
        this.notificationUseCase = notificationUseCase;
        this.activityLogRecorder = activityLogRecorder;
        this.mapper = mapper;
    }

    @Override
    public JobApplicationResponse create(JobApplicationRequest request) {
        if (request.getCandidateId() == null) {
            throw new BusinessRuleException("Candidate id is required");
        }
        if (repository.existsByCandidateAndJob(request.getCandidateId(), request.getJobId())) {
            throw new BusinessRuleException("Candidate has already applied to this job");
        }
        Job job = jobRepository.findById(request.getJobId());
        if (job.getStatus() != JobStatus.OPEN) {
            throw new BusinessRuleException("Candidate can only apply to an open job");
        }
        Instant now = Instant.now();
        JobApplication application = new JobApplication(null, request.getCandidateId(), request.getJobId(),
                request.getStageId(), INITIAL_STATUS,
                request.getSource(), request.getExpectedSalary(), request.getNote(), now, null);
        JobApplication savedApplication = repository.save(application);
        notificationUseCase.notifyApplicationCreated(job, savedApplication);
        activityLogRecorder.record(ActivityAction.CREATE, EntityType.APPLICATION, savedApplication.getId(),
                "Ứng tuyển job: " + job.getTitle());
        return mapper.toResponse(savedApplication);
    }

    @Override
    public JobApplicationResponse apply(Long candidateUserId, JobApplicationRequest request) {
        Candidate candidate = candidateRepository.findByUserId(candidateUserId);
        request.setCandidateId(candidate.getId());
        request.setStatus(null);
        return create(request);
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
        JobApplication savedApplication = repository.save(application);
        activityLogRecorder.record(ActivityAction.UPDATE, EntityType.APPLICATION, savedApplication.getId(),
                "Cập nhật đơn ứng tuyển #" + savedApplication.getId());
        return mapper.toResponse(savedApplication);
    }

    @Override
    public JobApplicationResponse changeStatus(Long id, ApplicationStatusRequest request) {
        JobApplication existing = repository.findById(id);
        ApplicationStatus oldStatus = existing.getStatus();
        validateStatusTransition(existing.getStatus(), request.getStatus());
        JobApplication application = existing.changeStatus(request.getStatus(), Instant.now());
        application.setStageId(request.getStageId());
        JobApplication savedApplication = repository.save(application);
        if (oldStatus != savedApplication.getStatus()) {
            Job job = jobRepository.findById(savedApplication.getJobId());
            Candidate candidate = candidateRepository.findById(savedApplication.getCandidateId());
            notificationUseCase.notifyApplicationStatusChanged(candidate.getUserId(), job, savedApplication, oldStatus);
            activityLogRecorder.record(ActivityAction.CHANGE_STATUS, EntityType.APPLICATION, savedApplication.getId(),
                    "Chuyển trạng thái đơn ứng tuyển #" + savedApplication.getId()
                            + " từ " + oldStatus + " sang " + savedApplication.getStatus());
        }
        return mapper.toResponse(savedApplication);
    }

    @Override
    public JobApplicationResponse withdraw(Long id, Long candidateUserId) {
        JobApplication existing = repository.findById(id);
        Candidate candidate = candidateRepository.findByUserId(candidateUserId);
        if (!existing.getCandidateId().equals(candidate.getId())) {
            throw new BusinessRuleException("Cannot withdraw another candidate's application");
        }
        if (existing.getStatus() == ApplicationStatus.HIRED || existing.getStatus() == ApplicationStatus.REJECTED) {
            throw new BusinessRuleException("Cannot withdraw an application after final decision");
        }
        if (existing.getStatus() == ApplicationStatus.WITHDRAWN) {
            return mapper.toResponse(existing);
        }
        ApplicationStatus oldStatus = existing.getStatus();
        JobApplication withdrawn = repository.save(existing.changeStatus(ApplicationStatus.WITHDRAWN, Instant.now()));
        activityLogRecorder.record(ActivityAction.CHANGE_STATUS, EntityType.APPLICATION, withdrawn.getId(),
                "Withdraw application #" + withdrawn.getId() + " from " + oldStatus + " to " + withdrawn.getStatus());
        return mapper.toResponse(withdrawn);
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
        JobApplication application = repository.findById(id);
        repository.deleteById(id);
        activityLogRecorder.record(ActivityAction.DELETE, EntityType.APPLICATION, id,
                "Xóa đơn ứng tuyển #" + application.getId());
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
