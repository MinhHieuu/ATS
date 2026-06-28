package com.example.ats.application.service;

import com.example.ats.application.dto.request.InterviewFeedbackRequest;
import com.example.ats.application.dto.response.InterviewFeedbackResponse;
import com.example.ats.application.mapper.InterviewFeedbackMapper;
import com.example.ats.application.port.in.InterviewFeedbackUseCase;
import com.example.ats.application.port.out.InterviewFeedbackRepository;
import com.example.ats.domain.model.InterviewFeedback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class InterviewFeedbackService implements InterviewFeedbackUseCase {
    private final InterviewFeedbackRepository repository;
    private final InterviewFeedbackMapper mapper;

    public InterviewFeedbackService(InterviewFeedbackRepository repository, InterviewFeedbackMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public InterviewFeedbackResponse create(InterviewFeedbackRequest request) {
        InterviewFeedback feedback = new InterviewFeedback(null, request.getInterviewId(), request.getRecruiterId(),
                request.getRating(), request.getComment(), request.getRecommendation(), Instant.now());
        return mapper.toResponse(repository.save(feedback));
    }

    @Override
    public InterviewFeedbackResponse update(Long id, InterviewFeedbackRequest request) {
        InterviewFeedback feedback = repository.findById(id);
        feedback.setInterviewId(request.getInterviewId());
        feedback.setRecruiterId(request.getRecruiterId());
        feedback.setRating(request.getRating());
        feedback.setComment(request.getComment());
        feedback.setRecommendation(request.getRecommendation());
        return mapper.toResponse(repository.save(feedback));
    }

    @Override
    @Transactional(readOnly = true)
    public InterviewFeedbackResponse findById(Long id) {
        return mapper.toResponse(repository.findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<InterviewFeedbackResponse> findAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
