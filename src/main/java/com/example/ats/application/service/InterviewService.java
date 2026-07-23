package com.example.ats.application.service;

import com.example.ats.application.dto.request.InterviewRequest;
import com.example.ats.application.dto.request.InterviewResultRequest;
import com.example.ats.application.dto.response.InterviewResponse;
import com.example.ats.application.port.in.InterviewUseCase;
import com.example.ats.application.port.in.NotificationUseCase;
import com.example.ats.application.port.out.InterviewRepository;
import com.example.ats.application.port.out.JobApplicationRepository;
import com.example.ats.domain.model.ApplicationStatus;
import com.example.ats.domain.model.Interview;
import com.example.ats.domain.model.InterviewResult;
import com.example.ats.domain.model.NotificationType;
import com.example.ats.domain.view.JobApplicationView;
import com.example.ats.domain.view.InterviewView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Transactional
public class InterviewService implements InterviewUseCase {
    private final InterviewRepository interviewRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final NotificationUseCase notificationUseCase;

    public InterviewService(InterviewRepository interviewRepository,
                            JobApplicationRepository jobApplicationRepository,
                            NotificationUseCase notificationUseCase) {
        this.interviewRepository = interviewRepository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.notificationUseCase = notificationUseCase;
    }

    // ── Recruiter ──────────────────────────────────────────────

    @Override
    public InterviewResponse createByJobOwner(InterviewRequest request, Long recruiterId) {
        jobApplicationRepository.findByIdAndJobCreatedBy(request.getApplicationId(), recruiterId);
        return doCreate(request);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InterviewResponse> findByJobOwner(Long recruiterId, Pageable pageable) {
        return interviewRepository.findByJobCreatedBy(recruiterId, pageable).map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public InterviewResponse findByIdAndJobOwner(Long id, Long recruiterId) {
        return toResponse(interviewRepository.findByIdAndJobCreatedBy(id, recruiterId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InterviewResponse> findByApplicationAndJobOwner(Long applicationId, Long recruiterId, Pageable pageable) {
        return interviewRepository.findByApplicationAndJobCreatedBy(applicationId, recruiterId, pageable)
                .map(this::toResponse);
    }

    @Override
    public InterviewResponse updateByJobOwner(Long id, InterviewRequest request, Long recruiterId) {
        InterviewView existing = interviewRepository.findByIdAndJobCreatedBy(id, recruiterId);
        jobApplicationRepository.findByIdAndJobCreatedBy(request.getApplicationId(), recruiterId);
        return doUpdate(existing, request);
    }

    @Override
    public InterviewResponse updateResultByJobOwner(Long id, InterviewResultRequest request, Long recruiterId) {
        InterviewView existing = interviewRepository.findByIdAndJobCreatedBy(id, recruiterId);
        return doUpdateResult(existing, request);
    }

    @Override
    public void deleteByJobOwner(Long id, Long recruiterId) {
        interviewRepository.findByIdAndJobCreatedBy(id, recruiterId);
        interviewRepository.deleteById(id);
    }

    // ── Admin ──────────────────────────────────────────────────

    @Override
    public InterviewResponse create(InterviewRequest request) {
        return doCreate(request);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InterviewResponse> findAll(Pageable pageable) {
        return interviewRepository.findAll(pageable).map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public InterviewResponse findById(Long id) {
        return toResponse(interviewRepository.findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InterviewResponse> findByApplication(Long applicationId, Pageable pageable) {
        return interviewRepository.findByApplication(applicationId, pageable).map(this::toResponse);
    }

    @Override
    public InterviewResponse update(Long id, InterviewRequest request) {
        InterviewView existing = interviewRepository.findById(id);
        return doUpdate(existing, request);
    }

    @Override
    public InterviewResponse updateResult(Long id, InterviewResultRequest request) {
        InterviewView existing = interviewRepository.findById(id);
        return doUpdateResult(existing, request);
    }

    @Override
    public void delete(Long id) {
        interviewRepository.deleteById(id);
    }

    // ── Candidate ──────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public Page<InterviewResponse> findMyInterviews(Long candidateId, Pageable pageable) {
        return interviewRepository.findByCandidate(candidateId, pageable).map(this::toCandidateResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public InterviewResponse findMyInterviewById(Long id, Long candidateId) {
        return toCandidateResponse(interviewRepository.findByIdAndCandidate(id, candidateId));
    }

    // ── Private helpers ────────────────────────────────────────

    private void validateSchedule(InterviewRequest request) {
        if (request.getEndAt() != null && request.getEndAt().isBefore(request.getScheduledAt())) {
            throw new IllegalArgumentException("End time must be after scheduled time");
        }
    }

    private void validateApplicationStatus(Long applicationId) {
        JobApplicationView app = jobApplicationRepository.findById(applicationId);
        ApplicationStatus status = app.application().getStatus();
        if (status != ApplicationStatus.SCREENING && status != ApplicationStatus.INTERVIEW) {
            throw new IllegalArgumentException(
                    "Cannot schedule interview for application with status " + status);
        }
    }

    private InterviewResponse doCreate(InterviewRequest request) {
        validateSchedule(request);
        validateApplicationStatus(request.getApplicationId());
        Instant now = Instant.now();
        Interview interview = new Interview(null, request.getApplicationId(),
                request.getTitle(), request.getScheduledAt(), request.getEndAt(),
                request.getLocation(), request.getType(), request.getMeetingLink(),
                request.getNotes(), InterviewResult.PENDING, now, now);

        InterviewView view = interviewRepository.save(request.getApplicationId(), interview);

        notificationUseCase.send(view.candidateUserId(),
                NotificationType.INTERVIEW_SCHEDULED,
                "Lịch phỏng vấn mới",
                "Bạn có lịch phỏng vấn cho vị trí " + view.jobTitle() + ": " + view.interview().getTitle(),
                view.interview().getId());

        return toResponse(view);
    }

    private InterviewResponse doUpdate(InterviewView existing, InterviewRequest request) {
        validateSchedule(request);
        Interview interview = existing.interview();
        interview.setApplicationId(request.getApplicationId());
        interview.setTitle(request.getTitle());
        interview.setScheduledAt(request.getScheduledAt());
        interview.setEndAt(request.getEndAt());
        interview.setLocation(request.getLocation());
        interview.setType(request.getType());
        interview.setMeetingLink(request.getMeetingLink());
        interview.setNotes(request.getNotes());
        interview.setUpdatedAt(Instant.now());

        InterviewView view = interviewRepository.save(request.getApplicationId(), interview);

        notificationUseCase.send(view.candidateUserId(),
                NotificationType.INTERVIEW_UPDATED,
                "Cập nhật lịch phỏng vấn",
                "Lịch phỏng vấn cho vị trí " + view.jobTitle() + " đã được cập nhật: " + view.interview().getTitle(),
                view.interview().getId());

        return toResponse(view);
    }

    private InterviewResponse doUpdateResult(InterviewView existing, InterviewResultRequest request) {
        Interview interview = existing.interview();
        interview.setResult(request.getResult());
        interview.setUpdatedAt(Instant.now());

        InterviewView view = interviewRepository.save(interview.getApplicationId(), interview);

        notificationUseCase.send(view.candidateUserId(),
                NotificationType.INTERVIEW_UPDATED,
                "Cập nhật kết quả phỏng vấn",
                "Kết quả phỏng vấn cho vị trí " + view.jobTitle() + " đã được cập nhật: " + view.interview().getResult(),
                view.interview().getId());

        return toResponse(view);
    }

    private InterviewResponse toResponse(InterviewView view) {
        Interview i = view.interview();
        return new InterviewResponse(i.getId(), i.getApplicationId(),
                view.candidateName(), view.jobTitle(),
                i.getTitle(), i.getScheduledAt(), i.getEndAt(),
                i.getLocation(), i.getType(), i.getMeetingLink(),
                i.getNotes(), i.getResult(), i.getCreatedAt(), i.getUpdatedAt());
    }

    private InterviewResponse toCandidateResponse(InterviewView view) {
        InterviewResponse response = toResponse(view);
        response.setNotes(null);
        return response;
    }
}
