package com.example.ats.infrastructure.persistence.adapter;

import com.example.ats.application.mapper.JobMapper;
import com.example.ats.application.port.out.JobRepository;
import com.example.ats.domain.exception.ResourceNotFoundException;
import com.example.ats.domain.model.Job;
import com.example.ats.infrastructure.persistence.entity.JobEntity;
import com.example.ats.infrastructure.persistence.entity.UserEntity;
import com.example.ats.infrastructure.persistence.repository.SpringDataJobRepository;
import com.example.ats.infrastructure.persistence.repository.SpringDataUserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JobAdapter implements JobRepository {
    private final SpringDataJobRepository repository;
    private final SpringDataUserRepository userRepository;
    private final JobMapper mapper;

    public JobAdapter(SpringDataJobRepository repository, SpringDataUserRepository userRepository, JobMapper mapper) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public Job save(Job job) {
        UserEntity createdBy = job.getCreatedBy() == null ? null : userRepository.findById(job.getCreatedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + job.getCreatedBy()));
        JobEntity entity = new JobEntity(job.getId(), job.getTitle(), job.getDescription(), job.getRequirements(),
                job.getLocation(), job.getEmploymentType(), job.getSalaryMin(), job.getSalaryMax(),
                job.getStatus(), createdBy, job.getCreatedAt(), job.getUpdatedAt(), null);
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public Job findById(Long id) {
        return mapper.toDomain(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found")));
    }

    @Override
    public List<Job> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Job not found");
        }
        repository.deleteById(id);
    }
}
