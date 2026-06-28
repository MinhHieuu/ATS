package com.example.ats.application.service;

import com.example.ats.application.dto.request.InterviewRequest;
import com.example.ats.application.dto.request.InterviewStatusRequest;
import com.example.ats.application.dto.response.InterviewResponse;
import com.example.ats.application.mapper.InterviewMapper;
import com.example.ats.application.port.in.InterviewUseCase;
import com.example.ats.application.port.out.InterviewRepository;
import com.example.ats.domain.model.Interview;
import com.example.ats.domain.model.InterviewStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class InterviewService implements InterviewUseCase {
    private final InterviewRepository repository;
    private final InterviewMapper mapper;

    public InterviewService(InterviewRepository repository, InterviewMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public InterviewResponse create(InterviewRequest request) {
        Interview interview = new Interview(null, request.getJobApplicationId(), request.getInterviewerId(),
                request.getTitle(), request.getInterviewTime(), request.getLocation(),
                request.getStatus() == null ? InterviewStatus.SCHEDULED : request.getStatus(), Instant.now());
        return mapper.toResponse(repository.save(interview));
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
        return mapper.toResponse(repository.save(interview));
    }

    @Override
    public InterviewResponse changeStatus(Long id, InterviewStatusRequest request) {
        return mapper.toResponse(repository.save(repository.findById(id).changInterview(request.getStatus())));
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
        repository.deleteById(id);
    }
}
