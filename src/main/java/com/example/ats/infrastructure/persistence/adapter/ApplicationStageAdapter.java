package com.example.ats.infrastructure.persistence.adapter;

import com.example.ats.application.mapper.ApplicationStageMapper;
import com.example.ats.application.port.out.ApplicationStageRepository;
import com.example.ats.domain.exception.ResourceNotFoundException;
import com.example.ats.domain.model.ApplicationStage;
import com.example.ats.infrastructure.persistence.entity.ApplicationStageEntity;
import com.example.ats.infrastructure.persistence.repository.SpringDataApplicationStageRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ApplicationStageAdapter implements ApplicationStageRepository {
    private final SpringDataApplicationStageRepository repository;
    private final ApplicationStageMapper mapper;

    public ApplicationStageAdapter(SpringDataApplicationStageRepository repository, ApplicationStageMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public ApplicationStage save(ApplicationStage stage) {
        return mapper.toDomain(repository.save(new ApplicationStageEntity(
                stage.getId(), stage.getName(), stage.getPosition(), null)));
    }

    @Override
    public ApplicationStage findById(Long id) {
        return mapper.toDomain(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application stage not found")));
    }

    @Override
    public List<ApplicationStage> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Application stage not found");
        }
        repository.deleteById(id);
    }
}
