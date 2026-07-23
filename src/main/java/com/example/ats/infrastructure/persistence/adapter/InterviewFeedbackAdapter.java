package com.example.ats.infrastructure.persistence.adapter;

import com.example.ats.application.port.out.InterviewFeedbackRepository;
import com.example.ats.domain.exception.ResourceNotFoundException;
import com.example.ats.domain.model.InterviewFeedback;
import com.example.ats.domain.view.InterviewFeedbackView;
import com.example.ats.infrastructure.persistence.entity.InterviewEntity;
import com.example.ats.infrastructure.persistence.entity.InterviewFeedbackEntity;
import com.example.ats.infrastructure.persistence.entity.UserEntity;
import com.example.ats.infrastructure.persistence.repository.SpringDataInterviewFeedbackRepository;
import com.example.ats.infrastructure.persistence.repository.SpringDataInterviewRepository;
import com.example.ats.infrastructure.persistence.repository.SpringDataUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class InterviewFeedbackAdapter implements InterviewFeedbackRepository {
    private static final String NOT_FOUND = "Interview feedback not found";

    private final SpringDataInterviewFeedbackRepository repository;
    private final SpringDataInterviewRepository interviewRepository;
    private final SpringDataUserRepository userRepository;

    public InterviewFeedbackAdapter(SpringDataInterviewFeedbackRepository repository,
                                    SpringDataInterviewRepository interviewRepository,
                                    SpringDataUserRepository userRepository) {
        this.repository = repository;
        this.interviewRepository = interviewRepository;
        this.userRepository = userRepository;
    }

    @Override
    public InterviewFeedbackView save(Long interviewId, Long reviewerId, InterviewFeedback feedback) {
        InterviewEntity interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Interview not found"));
        UserEntity reviewer = userRepository.findById(reviewerId)
                .orElseThrow(() -> new ResourceNotFoundException("Reviewer not found"));
        InterviewFeedbackEntity entity = new InterviewFeedbackEntity(feedback.getId(), interview, reviewer,
                feedback.getOverallRating(), feedback.getTechnicalRating(), feedback.getCommunicationRating(),
                feedback.getStrengths(), feedback.getWeaknesses(), feedback.getComments(),
                feedback.getRecommendation(), feedback.getCreatedAt(), feedback.getUpdatedAt());
        return toView(repository.save(entity));
    }

    @Override
    public Page<InterviewFeedbackView> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toView);
    }

    @Override
    public InterviewFeedbackView findById(Long id) {
        return toView(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND)));
    }

    @Override
    public Page<InterviewFeedbackView> findByInterview(Long interviewId, Pageable pageable) {
        return repository.findByInterview_Id(interviewId, pageable).map(this::toView);
    }

    @Override
    public Page<InterviewFeedbackView> findByJobCreatedBy(Long recruiterId, Pageable pageable) {
        return repository.findByJobCreatedBy(recruiterId, pageable).map(this::toView);
    }

    @Override
    public InterviewFeedbackView findByIdAndJobCreatedBy(Long id, Long recruiterId) {
        return toView(repository.findByIdAndJobCreatedBy(id, recruiterId)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND)));
    }

    @Override
    public Page<InterviewFeedbackView> findByInterviewAndJobCreatedBy(Long interviewId, Long recruiterId, Pageable pageable) {
        return repository.findByInterviewAndJobCreatedBy(interviewId, recruiterId, pageable).map(this::toView);
    }

    @Override
    public boolean existsByInterviewAndReviewer(Long interviewId, Long reviewerId) {
        return repository.existsByInterview_IdAndReviewer_Id(interviewId, reviewerId);
    }

    @Override
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(NOT_FOUND);
        }
        repository.deleteById(id);
    }

    private InterviewFeedbackView toView(InterviewFeedbackEntity entity) {
        InterviewFeedback feedback = new InterviewFeedback(entity.getId(),
                entity.getInterview().getId(),
                entity.getReviewer().getId(),
                entity.getOverallRating(), entity.getTechnicalRating(), entity.getCommunicationRating(),
                entity.getStrengths(), entity.getWeaknesses(), entity.getComments(),
                entity.getRecommendation(), entity.getCreatedAt(), entity.getUpdatedAt());

        String reviewerName = entity.getReviewer().getFullname();
        String interviewTitle = entity.getInterview().getTitle();
        String candidateName = entity.getInterview().getApplication().getCandidate().getUser().getFullname();
        String jobTitle = entity.getInterview().getApplication().getJob().getTitle();

        return new InterviewFeedbackView(feedback, reviewerName, interviewTitle, candidateName, jobTitle);
    }
}
