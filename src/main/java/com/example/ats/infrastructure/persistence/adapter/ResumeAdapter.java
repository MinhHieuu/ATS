package com.example.ats.infrastructure.persistence.adapter;

import com.example.ats.application.mapper.ResumeMapper;
import com.example.ats.application.port.out.ResumeRepository;
import com.example.ats.domain.exception.ResourceNotFoundException;
import com.example.ats.domain.model.Resume;
import com.example.ats.infrastructure.persistence.entity.CandidateEntity;
import com.example.ats.infrastructure.persistence.entity.ResumeEntity;
import com.example.ats.infrastructure.persistence.repository.SpringDataCandidateRepository;
import com.example.ats.infrastructure.persistence.repository.SpringDataResumeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class ResumeAdapter implements ResumeRepository {
    private final SpringDataResumeRepository repository;
    private final SpringDataCandidateRepository candidateRepository;
    private final ResumeMapper mapper;

    public ResumeAdapter(SpringDataResumeRepository repository, SpringDataCandidateRepository candidateRepository,
                         ResumeMapper mapper) {
        this.repository = repository;
        this.candidateRepository = candidateRepository;
        this.mapper = mapper;
    }

    @Override
    public Resume save(Resume resume) {
        CandidateEntity candidate = candidateRepository.findById(resume.getCandidateId())
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));
        ResumeEntity entity = new ResumeEntity();
        entity.setId(resume.getId());
        entity.setCandidate(candidate);
        entity.setFileName(resume.getFileName());
        entity.setFileUrl(resume.getFileUrl());
        entity.setUploadedAt(resume.getCreatedAt());
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public Resume findById(Long id) {
        return mapper.toDomain(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found")));
    }

    @Override
    public Page<Resume> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDomain);
    }

    @Override
    public Page<Resume> findByCandidate(Long candidateId, Pageable pageable) {
        return repository.findByCandidate_Id(candidateId, pageable).map(mapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Resume not found");
        }
        repository.deleteById(id);
    }
}
