package com.example.ats.application.service;

import com.example.ats.application.dto.request.ActivityLogRequest;
import com.example.ats.application.dto.response.ActivityLogResponse;
import com.example.ats.application.mapper.ActivityLogMapper;
import com.example.ats.application.port.in.ActivityLogUseCase;
import com.example.ats.application.port.out.ActivityLogRepository;
import com.example.ats.domain.model.ActivityLog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class ActivityLogService implements ActivityLogUseCase {
    private final ActivityLogRepository repository;
    private final ActivityLogMapper mapper;

    public ActivityLogService(ActivityLogRepository repository, ActivityLogMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public ActivityLogResponse create(ActivityLogRequest request) {
        ActivityLog log = new ActivityLog(null, request.getUserId(), request.getAction(), request.getEntityType(),
                request.getEntityId(), request.getDescription(), Instant.now());
        return mapper.toResponse(repository.save(log));
    }

    @Override
    @Transactional(readOnly = true)
    public ActivityLogResponse findById(Long id) {
        return mapper.toResponse(repository.findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityLogResponse> findAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityLogResponse> findByUser(Long userId) {
        return repository.findByUser(userId).stream().map(mapper::toResponse).toList();
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
