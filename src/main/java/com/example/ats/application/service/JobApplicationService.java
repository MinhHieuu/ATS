package com.example.ats.application.service;

import com.example.ats.application.dto.request.ApplicationStatusRequest;
import com.example.ats.application.dto.request.JobApplicationRequest;
import com.example.ats.application.dto.response.CandidateResponse;
import com.example.ats.application.dto.response.JobApplicationResponse;
import com.example.ats.application.dto.response.JobResponse;
import com.example.ats.application.mapper.CandidateMapper;
import com.example.ats.application.mapper.CompanyMapper;
import com.example.ats.application.mapper.JobApplicationMapper;
import com.example.ats.application.mapper.JobMapper;
import com.example.ats.application.mapper.ResumeMapper;
import com.example.ats.application.mapper.UserMapper;
import com.example.ats.application.port.in.JobApplicationUseCase;
import com.example.ats.application.port.in.NotificationUseCase;
import com.example.ats.application.port.out.JobApplicationRepository;
import com.example.ats.application.port.out.JobRepository;
import com.example.ats.application.port.out.ResumeRepository;
import com.example.ats.domain.exception.BusinessRuleException;
import com.example.ats.domain.exception.ResourceNotFoundException;
import com.example.ats.domain.model.ApplicationStatus;
import com.example.ats.domain.model.Job;
import com.example.ats.domain.model.JobApplication;
import com.example.ats.domain.model.JobStatus;
import com.example.ats.domain.model.NotificationType;
import com.example.ats.domain.model.Resume;
import com.example.ats.domain.view.JobApplicationView;
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
    private final CandidateMapper candidateMapper;
    private final UserMapper userMapper;
    private final JobMapper jobMapper;
    private final CompanyMapper companyMapper;
    private final ResumeMapper resumeMapper;
    private final NotificationUseCase notificationUseCase;

    public JobApplicationService(JobApplicationRepository repository, JobRepository jobRepository,
                                 ResumeRepository resumeRepository, JobApplicationMapper mapper,
                                 CandidateMapper candidateMapper, UserMapper userMapper,
                                 JobMapper jobMapper, CompanyMapper companyMapper,
                                 ResumeMapper resumeMapper, NotificationUseCase notificationUseCase) {
        this.repository = repository;
        this.jobRepository = jobRepository;
        this.resumeRepository = resumeRepository;
        this.mapper = mapper;
        this.candidateMapper = candidateMapper;
        this.userMapper = userMapper;
        this.jobMapper = jobMapper;
        this.companyMapper = companyMapper;
        this.resumeMapper = resumeMapper;
        this.notificationUseCase = notificationUseCase;
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
        JobApplicationView view = repository.save(application);
        notifyApplicationReceived(view);
        return toResponse(view);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobApplicationResponse> findMyApplications(Long candidateId, Pageable pageable) {
        return repository.findByCandidate(candidateId, pageable).map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public JobApplicationResponse findMyApplicationById(Long id, Long candidateId) {
        return toResponse(repository.findByIdAndCandidate(id, candidateId));
    }

    @Override
    public JobApplicationResponse withdraw(Long id, Long candidateId) {
        JobApplicationView view = repository.findByIdAndCandidate(id, candidateId);
        JobApplicationView savedView = changeStatus(view.application(), ApplicationStatus.WITHDRAWN);
        notifyApplicationWithdrawn(savedView);
        return toResponse(savedView);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobApplicationResponse> findByJobOwner(Long recruiterId, Pageable pageable) {
        return repository.findByJobCreatedBy(recruiterId, pageable).map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobApplicationResponse> findByJobAndJobOwner(Long jobId, Long recruiterId, Pageable pageable) {
        return repository.findByJobAndJobCreatedBy(jobId, recruiterId, pageable).map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public JobApplicationResponse findByIdAndJobOwner(Long id, Long recruiterId) {
        return toResponse(repository.findByIdAndJobCreatedBy(id, recruiterId));
    }

    @Override
    public JobApplicationResponse updateStatusByJobOwner(Long id, ApplicationStatusRequest request, Long recruiterId) {
        rejectWithdrawnTarget(request.getStatus());
        JobApplicationView view = repository.findByIdAndJobCreatedBy(id, recruiterId);
        JobApplicationView savedView = changeStatus(view.application(), request.getStatus());
        notifyStatusChanged(savedView);
        return toResponse(savedView);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobApplicationResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public JobApplicationResponse findById(Long id) {
        return toResponse(repository.findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobApplicationResponse> findByJob(Long jobId, Pageable pageable) {
        return repository.findByJob(jobId, pageable).map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobApplicationResponse> findByCandidate(Long candidateId, Pageable pageable) {
        return repository.findByCandidate(candidateId, pageable).map(this::toResponse);
    }

    @Override
    public JobApplicationResponse updateStatus(Long id, ApplicationStatusRequest request) {
        rejectWithdrawnTarget(request.getStatus());
        JobApplicationView view = repository.findById(id);
        JobApplicationView savedView = changeStatus(view.application(), request.getStatus());
        notifyStatusChanged(savedView);
        return toResponse(savedView);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private JobApplicationView changeStatus(JobApplication application, ApplicationStatus newStatus) {
        validateStatusTransition(application.getStatus(), newStatus);
        application.setStatus(newStatus);
        application.setUpdatedAt(Instant.now());
        return repository.save(application);
    }

    private void rejectWithdrawnTarget(ApplicationStatus newStatus) {
        if (newStatus == ApplicationStatus.WITHDRAWN) {
            throw new BusinessRuleException("Only the candidate can withdraw an application");
        }
    }

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

    private void notifyApplicationReceived(JobApplicationView view) {
        String candidateName = view.candidateUser() != null ? view.candidateUser().getFullname() : "Ứng viên";
        String jobTitle = view.job().getTitle();
        String title = "Đơn ứng tuyển mới";
        String content = candidateName + " đã ứng tuyển vào job: " + jobTitle;
        Long applicationId = view.application().getId();
        Long creatorId = null;
        if (view.jobCreatedBy() != null) {
            creatorId = view.jobCreatedBy().getId();
            notificationUseCase.send(creatorId,
                    NotificationType.APPLICATION_RECEIVED, title, content, applicationId);
        }
        notificationUseCase.sendToAdmins(NotificationType.APPLICATION_RECEIVED, title, content, applicationId, creatorId);
    }

    private void notifyApplicationWithdrawn(JobApplicationView view) {
        String candidateName = view.candidateUser() != null ? view.candidateUser().getFullname() : "Ứng viên";
        String jobTitle = view.job().getTitle();
        String title = "Ứng viên rút đơn";
        String content = candidateName + " đã rút đơn ứng tuyển job: " + jobTitle;
        Long applicationId = view.application().getId();
        Long creatorId = null;
        if (view.jobCreatedBy() != null) {
            creatorId = view.jobCreatedBy().getId();
            notificationUseCase.send(creatorId,
                    NotificationType.APPLICATION_WITHDRAWN, title, content, applicationId);
        }
        notificationUseCase.sendToAdmins(NotificationType.APPLICATION_WITHDRAWN, title, content, applicationId, creatorId);
    }

    private void notifyStatusChanged(JobApplicationView view) {
        String jobTitle = view.job().getTitle();
        ApplicationStatus status = view.application().getStatus();
        String title = "Cập nhật trạng thái đơn ứng tuyển";
        String content = "Đơn ứng tuyển job " + jobTitle + " đã chuyển sang: " + status;
        notificationUseCase.send(view.candidateUser().getId(),
                NotificationType.APPLICATION_STATUS_CHANGED, title, content, view.application().getId());
    }

    private JobApplicationResponse toResponse(JobApplicationView view) {
        JobApplicationResponse response = mapper.toResponse(view.application());

        CandidateResponse candidateResponse = candidateMapper.toResponse(view.candidate());
        if (view.candidateUser() != null) {
            candidateResponse.setUser(userMapper.toResponse(view.candidateUser()));
        }
        response.setCandidate(candidateResponse);

        JobResponse jobResponse = jobMapper.toResponse(view.job());
        if (view.jobCompany() != null) {
            jobResponse.setCompany(companyMapper.toResponse(view.jobCompany()));
        }
        if (view.jobCreatedBy() != null) {
            jobResponse.setCreatedBy(userMapper.toResponse(view.jobCreatedBy()));
        }
        response.setJob(jobResponse);

        if (view.resume() != null) {
            response.setResume(resumeMapper.toResponse(view.resume()));
        }

        return response;
    }
}
