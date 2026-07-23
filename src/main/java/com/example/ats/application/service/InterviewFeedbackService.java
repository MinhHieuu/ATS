package com.example.ats.application.service;

import com.example.ats.application.dto.request.InterviewFeedbackRequest;
import com.example.ats.application.dto.response.InterviewFeedbackResponse;
import com.example.ats.application.port.in.InterviewFeedbackUseCase;
import com.example.ats.application.port.out.InterviewFeedbackRepository;
import com.example.ats.application.port.out.InterviewRepository;
import com.example.ats.domain.exception.BusinessRuleException;
import com.example.ats.domain.model.InterviewFeedback;
import com.example.ats.domain.model.InterviewResult;
import com.example.ats.domain.view.InterviewFeedbackView;
import com.example.ats.domain.view.InterviewView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Transactional
public class InterviewFeedbackService implements InterviewFeedbackUseCase {
    private final InterviewFeedbackRepository feedbackRepository;
    private final InterviewRepository interviewRepository;

    public InterviewFeedbackService(InterviewFeedbackRepository feedbackRepository,
                                    InterviewRepository interviewRepository) {
        this.feedbackRepository = feedbackRepository;
        this.interviewRepository = interviewRepository;
    }

    // ── Recruiter ──────────────────────────────────────────────

    @Override
    public InterviewFeedbackResponse createByJobOwner(InterviewFeedbackRequest request, Long recruiterId) {
        InterviewView interview = interviewRepository.findByIdAndJobCreatedBy(request.getInterviewId(), recruiterId);
        validateInterviewState(interview);

        if (feedbackRepository.existsByInterviewAndReviewer(request.getInterviewId(), recruiterId)) {
            throw new BusinessRuleException("Feedback for this interview has already been submitted");
        }

        Instant now = Instant.now();
        InterviewFeedback feedback = new InterviewFeedback(null, request.getInterviewId(), recruiterId,
                request.getOverallRating(), request.getTechnicalRating(), request.getCommunicationRating(),
                request.getStrengths(), request.getWeaknesses(), request.getComments(),
                request.getRecommendation(), now, now);

        return toResponse(feedbackRepository.save(request.getInterviewId(), recruiterId, feedback));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InterviewFeedbackResponse> findByJobOwner(Long recruiterId, Pageable pageable) {
        return feedbackRepository.findByJobCreatedBy(recruiterId, pageable).map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public InterviewFeedbackResponse findByIdAndJobOwner(Long id, Long recruiterId) {
        return toResponse(feedbackRepository.findByIdAndJobCreatedBy(id, recruiterId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InterviewFeedbackResponse> findByInterviewAndJobOwner(Long interviewId, Long recruiterId, Pageable pageable) {
        return feedbackRepository.findByInterviewAndJobCreatedBy(interviewId, recruiterId, pageable)
                .map(this::toResponse);
    }

    @Override
    public InterviewFeedbackResponse updateByReviewer(Long id, InterviewFeedbackRequest request, Long recruiterId) {
        InterviewFeedbackView existing = feedbackRepository.findByIdAndJobCreatedBy(id, recruiterId);
        InterviewFeedback feedback = existing.feedback();
        validateReviewer(feedback, recruiterId);

        if (!feedback.getInterviewId().equals(request.getInterviewId())) {
            throw new BusinessRuleException("Interview cannot be changed on an existing feedback");
        }
        validateInterviewState(interviewRepository.findByIdAndJobCreatedBy(feedback.getInterviewId(), recruiterId));

        feedback.setOverallRating(request.getOverallRating());
        feedback.setTechnicalRating(request.getTechnicalRating());
        feedback.setCommunicationRating(request.getCommunicationRating());
        feedback.setStrengths(request.getStrengths());
        feedback.setWeaknesses(request.getWeaknesses());
        feedback.setComments(request.getComments());
        feedback.setRecommendation(request.getRecommendation());
        feedback.setUpdatedAt(Instant.now());

        return toResponse(feedbackRepository.save(feedback.getInterviewId(), feedback.getReviewerId(), feedback));
    }

    @Override
    public void deleteByReviewer(Long id, Long recruiterId) {
        InterviewFeedbackView existing = feedbackRepository.findByIdAndJobCreatedBy(id, recruiterId);
        validateReviewer(existing.feedback(), recruiterId);
        feedbackRepository.deleteById(id);
    }

    // ── Admin ──────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public Page<InterviewFeedbackResponse> findAll(Pageable pageable) {
        return feedbackRepository.findAll(pageable).map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public InterviewFeedbackResponse findById(Long id) {
        return toResponse(feedbackRepository.findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InterviewFeedbackResponse> findByInterview(Long interviewId, Pageable pageable) {
        return feedbackRepository.findByInterview(interviewId, pageable).map(this::toResponse);
    }

    @Override
    public void delete(Long id) {
        feedbackRepository.deleteById(id);
    }

    // ── Private helpers ────────────────────────────────────────

    private void validateInterviewState(InterviewView view) {
        InterviewResult result = view.interview().getResult();
        if (result == InterviewResult.CANCELLED) {
            throw new BusinessRuleException("Cannot give feedback for a cancelled interview");
        }
        if (view.interview().getScheduledAt().isAfter(Instant.now())) {
            throw new BusinessRuleException("Cannot give feedback before the interview takes place");
        }
    }

    private void validateReviewer(InterviewFeedback feedback, Long recruiterId) {
        if (!feedback.getReviewerId().equals(recruiterId)) {
            throw new BusinessRuleException("Only the reviewer can modify this feedback");
        }
    }

    private InterviewFeedbackResponse toResponse(InterviewFeedbackView view) {
        InterviewFeedback f = view.feedback();
        return new InterviewFeedbackResponse(f.getId(), f.getInterviewId(), view.interviewTitle(),
                f.getReviewerId(), view.reviewerName(), view.candidateName(), view.jobTitle(),
                f.getOverallRating(), f.getTechnicalRating(), f.getCommunicationRating(),
                f.getStrengths(), f.getWeaknesses(), f.getComments(),
                f.getRecommendation(), f.getCreatedAt(), f.getUpdatedAt());
    }
}
