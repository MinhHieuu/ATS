package com.example.ats.infrastructure.persistence.adapter;

import com.example.ats.application.port.out.InterviewRepository;
import com.example.ats.domain.exception.ResourceNotFoundException;
import com.example.ats.domain.model.Interview;
import com.example.ats.domain.model.InterviewResult;
import com.example.ats.domain.view.InterviewView;
import com.example.ats.infrastructure.persistence.entity.ApplicationEntity;
import com.example.ats.infrastructure.persistence.entity.InterviewEntity;
import com.example.ats.infrastructure.persistence.repository.SpringDataApplicationRepository;
import com.example.ats.infrastructure.persistence.repository.SpringDataInterviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class InterviewAdapter implements InterviewRepository {
    private static final String NOT_FOUND = "Interview not found";

    private final SpringDataInterviewRepository repository;
    private final SpringDataApplicationRepository applicationRepository;

    public InterviewAdapter(SpringDataInterviewRepository repository,
                            SpringDataApplicationRepository applicationRepository) {
        this.repository = repository;
        this.applicationRepository = applicationRepository;
    }

    @Override
    public InterviewView save(Long applicationId, Interview interview) {
        ApplicationEntity application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        InterviewEntity entity = new InterviewEntity(interview.getId(), application,
                interview.getTitle(), interview.getScheduledAt(), interview.getEndAt(),
                interview.getLocation(), interview.getType(), interview.getMeetingLink(),
                interview.getNotes(), interview.getResult(),
                interview.getCreatedAt(), interview.getUpdatedAt());
        return toView(repository.save(entity));
    }

    @Override
    public Page<InterviewView> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toView);
    }

    @Override
    public InterviewView findById(Long id) {
        return toView(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND)));
    }

    @Override
    public Page<InterviewView> findByApplication(Long applicationId, Pageable pageable) {
        return repository.findByApplication_Id(applicationId, pageable).map(this::toView);
    }

    @Override
    public Page<InterviewView> findByJobCreatedBy(Long recruiterId, Pageable pageable) {
        return repository.findByJobCreatedBy(recruiterId, pageable).map(this::toView);
    }

    @Override
    public InterviewView findByIdAndJobCreatedBy(Long id, Long recruiterId) {
        return toView(repository.findByIdAndJobCreatedBy(id, recruiterId)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND)));
    }

    @Override
    public Page<InterviewView> findByApplicationAndJobCreatedBy(Long applicationId, Long recruiterId, Pageable pageable) {
        return repository.findByApplicationAndJobCreatedBy(applicationId, recruiterId, pageable).map(this::toView);
    }

    @Override
    public Page<InterviewView> findByCandidate(Long candidateId, Pageable pageable) {
        return repository.findByCandidate(candidateId, pageable).map(this::toView);
    }

    @Override
    public InterviewView findByIdAndCandidate(Long id, Long candidateId) {
        return toView(repository.findByIdAndCandidate(id, candidateId)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND)));
    }

    @Override
    public boolean existsByApplicationAndResult(Long applicationId, InterviewResult result) {
        return repository.existsByApplication_IdAndResult(applicationId, result);
    }

    @Override
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(NOT_FOUND);
        }
        repository.deleteById(id);
    }

    private InterviewView toView(InterviewEntity entity) {
        Interview interview = new Interview(entity.getId(),
                entity.getApplication().getId(),
                entity.getTitle(), entity.getScheduledAt(), entity.getEndAt(),
                entity.getLocation(), entity.getType(), entity.getMeetingLink(),
                entity.getNotes(), entity.getResult(),
                entity.getCreatedAt(), entity.getUpdatedAt());

        Long candidateUserId = entity.getApplication().getCandidate().getUser().getId();
        String candidateName = entity.getApplication().getCandidate().getUser().getFullname();
        String jobTitle = entity.getApplication().getJob().getTitle();

        return new InterviewView(interview, candidateUserId, candidateName, jobTitle);
    }
}
