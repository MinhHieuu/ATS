package com.example.ats.infrastructure.persistence.adapter;

import com.example.ats.application.mapper.InterviewFeedbackMapper;
import com.example.ats.application.port.out.InterviewFeedbackRepository;
import com.example.ats.domain.exception.ResourceNotFoundException;
import com.example.ats.domain.model.InterviewFeedback;
import com.example.ats.infrastructure.persistence.entity.InterviewEntity;
import com.example.ats.infrastructure.persistence.entity.InterviewFeedbackEntity;
import com.example.ats.infrastructure.persistence.entity.RecruiterEntity;
import com.example.ats.infrastructure.persistence.repository.SpringDataInterviewFeedbackRepository;
import com.example.ats.infrastructure.persistence.repository.SpringDataInterviewRepository;
import com.example.ats.infrastructure.persistence.repository.SpringDataRecruiterRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InterviewFeedbackAdapter implements InterviewFeedbackRepository {
    private final SpringDataInterviewFeedbackRepository repository;
    private final SpringDataInterviewRepository interviewRepository;
    private final SpringDataRecruiterRepository recruiterRepository;
    private final InterviewFeedbackMapper mapper;

    public InterviewFeedbackAdapter(SpringDataInterviewFeedbackRepository repository,
                                    SpringDataInterviewRepository interviewRepository,
                                    SpringDataRecruiterRepository recruiterRepository,
                                    InterviewFeedbackMapper mapper) {
        this.repository = repository;
        this.interviewRepository = interviewRepository;
        this.recruiterRepository = recruiterRepository;
        this.mapper = mapper;
    }

    @Override
    public InterviewFeedback save(InterviewFeedback feedback) {
        InterviewEntity interview = interviewRepository.findById(feedback.getInterviewId())
                .orElseThrow(() -> new ResourceNotFoundException("Interview not found"));
        RecruiterEntity reviewer = recruiterRepository.findById(feedback.getRecruiterId())
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter not found"));
        InterviewFeedbackEntity entity = new InterviewFeedbackEntity();
        entity.setId(feedback.getId());
        entity.setInterview(interview);
        entity.setReviewer(reviewer);
        entity.setRating(feedback.getRating());
        entity.setComment(feedback.getComment());
        entity.setRecommendation(feedback.getRecommendation());
        entity.setCreatedAt(feedback.getCreatedAt());
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public InterviewFeedback findById(Long id) {
        return mapper.toDomain(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Interview feedback not found")));
    }

    @Override
    public List<InterviewFeedback> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Interview feedback not found");
        }
        repository.deleteById(id);
    }
}
