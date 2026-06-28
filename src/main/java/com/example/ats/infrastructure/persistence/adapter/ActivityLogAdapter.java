package com.example.ats.infrastructure.persistence.adapter;

import com.example.ats.application.mapper.ActivityLogMapper;
import com.example.ats.application.port.out.ActivityLogRepository;
import com.example.ats.domain.exception.ResourceNotFoundException;
import com.example.ats.domain.model.ActivityLog;
import com.example.ats.infrastructure.persistence.entity.ActivityLogEntity;
import com.example.ats.infrastructure.persistence.entity.UserEntity;
import com.example.ats.infrastructure.persistence.repository.SpringDataActivityLogRepository;
import com.example.ats.infrastructure.persistence.repository.SpringDataUserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ActivityLogAdapter implements ActivityLogRepository {
    private final SpringDataActivityLogRepository repository;
    private final SpringDataUserRepository userRepository;
    private final ActivityLogMapper mapper;

    public ActivityLogAdapter(SpringDataActivityLogRepository repository, SpringDataUserRepository userRepository,
                              ActivityLogMapper mapper) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public ActivityLog save(ActivityLog log) {
        UserEntity user = log.getUserId() == null ? null : userRepository.findById(log.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        ActivityLogEntity entity = new ActivityLogEntity();
        entity.setId(log.getId());
        entity.setUser(user);
        entity.setAction(log.getAction());
        entity.setEntityType(log.getEntityType());
        entity.setEntityId(log.getEntityId());
        entity.setDescription(log.getDescription());
        entity.setCreatedAt(log.getCreatedAt());
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public ActivityLog findById(Long id) {
        return mapper.toDomain(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity log not found")));
    }

    @Override
    public List<ActivityLog> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<ActivityLog> findByUser(Long userId) {
        return repository.findByUser_IdOrderByCreatedAtDesc(userId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Activity log not found");
        }
        repository.deleteById(id);
    }
}
