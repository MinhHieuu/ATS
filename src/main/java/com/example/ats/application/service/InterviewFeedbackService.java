package com.example.ats.application.service;

import com.example.ats.application.dto.request.InterviewFeedbackRequest;
import com.example.ats.application.dto.response.InterviewFeedbackResponse;
import com.example.ats.application.mapper.InterviewFeedbackMapper;
import com.example.ats.application.port.in.InterviewFeedbackUseCase;
import com.example.ats.application.port.out.InterviewFeedbackRepository;
import com.example.ats.domain.model.ActivityAction;
import com.example.ats.domain.model.EntityType;
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
    private final ActivityLogRecorder activityLogRecorder;

    public InterviewFeedbackService(InterviewFeedbackRepository repository, InterviewFeedbackMapper mapper,
                                    ActivityLogRecorder activityLogRecorder) {
        this.repository = repository;
        this.mapper = mapper;
        this.activityLogRecorder = activityLogRecorder;
    }

    @Override
    public InterviewFeedbackResponse create(InterviewFeedbackRequest request) {
        InterviewFeedback feedback = new InterviewFeedback(null, request.getInterviewId(), request.getRecruiterId(),
                request.getRating(), request.getComment(), request.getRecommendation(), Instant.now());
        InterviewFeedback savedFeedback = repository.save(feedback);
        activityLogRecorder.record(ActivityAction.CREATE, EntityType.INTERVIEW, savedFeedback.getInterviewId(),
                "Thêm đánh giá phỏng vấn #" + savedFeedback.getInterviewId());
        return mapper.toResponse(savedFeedback);
    }

    @Override
    public InterviewFeedbackResponse update(Long id, InterviewFeedbackRequest request) {
        InterviewFeedback feedback = repository.findById(id);
        feedback.setInterviewId(request.getInterviewId());
        feedback.setRecruiterId(request.getRecruiterId());
        feedback.setRating(request.getRating());
        feedback.setComment(request.getComment());
        feedback.setRecommendation(request.getRecommendation());
        InterviewFeedback savedFeedback = repository.save(feedback);
        activityLogRecorder.record(ActivityAction.UPDATE, EntityType.INTERVIEW, savedFeedback.getInterviewId(),
                "Cập nhật đánh giá phỏng vấn #" + savedFeedback.getInterviewId());
        return mapper.toResponse(savedFeedback);
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
        InterviewFeedback feedback = repository.findById(id);
        repository.deleteById(id);
        activityLogRecorder.record(ActivityAction.DELETE, EntityType.INTERVIEW, feedback.getInterviewId(),
                "Xóa đánh giá phỏng vấn #" + feedback.getInterviewId());
    }
}
