package com.example.ats.application.service;

import com.example.ats.application.dto.request.ApplicationStageRequest;
import com.example.ats.application.dto.response.ApplicationStageResponse;
import com.example.ats.application.mapper.ApplicationStageMapper;
import com.example.ats.application.port.in.ApplicationStageUseCase;
import com.example.ats.application.port.out.ApplicationStageRepository;
import com.example.ats.domain.model.ApplicationStage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ApplicationStageService implements ApplicationStageUseCase {
    private final ApplicationStageRepository repository;
    private final ApplicationStageMapper mapper;

    public ApplicationStageService(ApplicationStageRepository repository, ApplicationStageMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public ApplicationStageResponse create(ApplicationStageRequest request) {
        return mapper.toResponse(repository.save(new ApplicationStage(null, request.getName(), request.getPosition())));
    }

    @Override
    public ApplicationStageResponse update(Long id, ApplicationStageRequest request) {
        ApplicationStage stage = repository.findById(id);
        stage.setName(request.getName());
        stage.setPosition(request.getPosition());
        return mapper.toResponse(repository.save(stage));
    }

    @Override
    @Transactional(readOnly = true)
    public ApplicationStageResponse findById(Long id) {
        return mapper.toResponse(repository.findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationStageResponse> findAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
