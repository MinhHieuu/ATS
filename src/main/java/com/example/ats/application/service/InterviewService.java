package com.example.ats.application.service;

import com.example.ats.application.dto.request.InterviewRequest;
import com.example.ats.application.dto.request.InterviewStatusRequest;
import com.example.ats.application.dto.response.InterviewResponse;
import com.example.ats.application.mapper.InterviewMapper;
import com.example.ats.application.port.in.InterviewUseCase;
import com.example.ats.application.port.out.JobApplicationRepository;
import com.example.ats.domain.exception.BusinessRuleException;
import com.example.ats.application.port.out.InterviewRepository;
import com.example.ats.domain.model.ActivityAction;
import com.example.ats.domain.model.ApplicationStatus;
import com.example.ats.domain.model.EntityType;
import com.example.ats.domain.model.Interview;
import com.example.ats.domain.model.InterviewStatus;
import com.example.ats.domain.model.JobApplication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class InterviewService implements InterviewUseCase {
    private final InterviewRepository repository;
    private final JobApplicationRepository jobApplicationRepository;
    private final InterviewMapper mapper;
    private final ActivityLogRecorder activityLogRecorder;

    public InterviewService(InterviewRepository repository, JobApplicationRepository jobApplicationRepository,
                            InterviewMapper mapper,
                            ActivityLogRecorder activityLogRecorder) {
        this.repository = repository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.mapper = mapper;
        this.activityLogRecorder = activityLogRecorder;
    }

    @Override
    public InterviewResponse create(InterviewRequest request) {
        JobApplication application = jobApplicationRepository.findById(request.getJobApplicationId());
        if (application.getStatus() != ApplicationStatus.INTERVIEW) {
            throw new BusinessRuleException("Interview can only be scheduled for applications in INTERVIEW status");
        }
        Interview interview = new Interview(null, request.getJobApplicationId(), request.getInterviewerId(),
                request.getTitle(), request.getInterviewTime(), request.getLocation(),
                request.getStatus() == null ? InterviewStatus.SCHEDULED : request.getStatus(), Instant.now());
        Interview savedInterview = repository.save(interview);
        activityLogRecorder.record(ActivityAction.CREATE, EntityType.INTERVIEW, savedInterview.getId(),
                "Tạo lịch phỏng vấn: " + savedInterview.getTitle());
        return mapper.toResponse(savedInterview);
    }

    @Override
    public InterviewResponse update(Long id, InterviewRequest request) {
        Interview interview = repository.findById(id);
        interview.setJobApplicationId(request.getJobApplicationId());
        interview.setInterviewerId(request.getInterviewerId());
        interview.setTitle(request.getTitle());
        interview.setInterviewTime(request.getInterviewTime());
        interview.setLocation(request.getLocation());
        interview.setStatus(request.getStatus() == null ? interview.getStatus() : request.getStatus());
        Interview savedInterview = repository.save(interview);
        activityLogRecorder.record(ActivityAction.UPDATE, EntityType.INTERVIEW, savedInterview.getId(),
                "Cập nhật lịch phỏng vấn: " + savedInterview.getTitle());
        return mapper.toResponse(savedInterview);
    }

    @Override
    public InterviewResponse changeStatus(Long id, InterviewStatusRequest request) {
        Interview interview = repository.findById(id);
        InterviewStatus oldStatus = interview.getStatus();
        Interview savedInterview = repository.save(interview.changInterview(request.getStatus()));
        if (oldStatus != savedInterview.getStatus()) {
            activityLogRecorder.record(ActivityAction.CHANGE_STATUS, EntityType.INTERVIEW, savedInterview.getId(),
                    "Chuyển trạng thái phỏng vấn #" + savedInterview.getId()
                            + " từ " + oldStatus + " sang " + savedInterview.getStatus());
        }
        return mapper.toResponse(savedInterview);
    }

    @Override
    @Transactional(readOnly = true)
    public InterviewResponse findById(Long id) {
        return mapper.toResponse(repository.findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<InterviewResponse> findAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<InterviewResponse> findByApplication(Long applicationId) {
        return repository.findByApplication(applicationId).stream().map(mapper::toResponse).toList();
    }

    @Override
    public void delete(Long id) {
        Interview interview = repository.findById(id);
        repository.deleteById(id);
        activityLogRecorder.record(ActivityAction.DELETE, EntityType.INTERVIEW, id,
                "Xóa lịch phỏng vấn: " + interview.getTitle());
    }
}
