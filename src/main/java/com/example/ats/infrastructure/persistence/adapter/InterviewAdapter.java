package com.example.ats.infrastructure.persistence.adapter;

import com.example.ats.application.mapper.InterviewMapper;
import com.example.ats.application.port.out.InterviewRepository;
import com.example.ats.domain.exception.ResourceNotFoundException;
import com.example.ats.domain.model.Interview;
import com.example.ats.infrastructure.persistence.entity.ApplicationEntity;
import com.example.ats.infrastructure.persistence.entity.InterviewEntity;
import com.example.ats.infrastructure.persistence.entity.RecruiterEntity;
import com.example.ats.infrastructure.persistence.repository.SpringDataApplicationRepository;
import com.example.ats.infrastructure.persistence.repository.SpringDataInterviewRepository;
import com.example.ats.infrastructure.persistence.repository.SpringDataRecruiterRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InterviewAdapter implements InterviewRepository {
    private final SpringDataInterviewRepository repository;
    private final SpringDataApplicationRepository applicationRepository;
    private final SpringDataRecruiterRepository recruiterRepository;
    private final InterviewMapper mapper;

    public InterviewAdapter(SpringDataInterviewRepository repository,
                            SpringDataApplicationRepository applicationRepository,
                            SpringDataRecruiterRepository recruiterRepository,
                            InterviewMapper mapper) {
        this.repository = repository;
        this.applicationRepository = applicationRepository;
        this.recruiterRepository = recruiterRepository;
        this.mapper = mapper;
    }

    @Override
    public Interview save(Interview interview) {
        ApplicationEntity application = applicationRepository.findById(interview.getJobApplicationId())
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        RecruiterEntity interviewer = interview.getInterviewerId() == null ? null : recruiterRepository.findById(interview.getInterviewerId())
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter not found"));
        InterviewEntity entity = new InterviewEntity();
        entity.setId(interview.getId());
        entity.setApplication(application);
        entity.setInterviewer(interviewer);
        entity.setTitle(interview.getTitle());
        entity.setInterviewTime(interview.getInterviewTime());
        entity.setLocation(interview.getLocation());
        entity.setStatus(interview.getStatus() == null ? null : interview.getStatus().name());
        entity.setCreatedAt(interview.getCreatedAt());
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public Interview findById(Long id) {
        return mapper.toDomain(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Interview not found")));
    }

    @Override
    public List<Interview> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Interview> findByApplication(Long applicationId) {
        return repository.findByApplication_Id(applicationId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Interview not found");
        }
        repository.deleteById(id);
    }
}
