package com.example.ats.application.service;

import com.example.ats.application.dto.request.ResumeRequest;
import com.example.ats.application.dto.response.ResumeResponse;
import com.example.ats.application.mapper.ResumeMapper;
import com.example.ats.application.port.in.ResumeUseCase;
import com.example.ats.application.port.out.ResumeRepository;
import com.example.ats.domain.model.Resume;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class ResumeService implements ResumeUseCase {
    private final ResumeRepository repository;
    private final ResumeMapper mapper;

    public ResumeService(ResumeRepository repository, ResumeMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public ResumeResponse create(ResumeRequest request) {
        Resume resume = new Resume(null, request.getCandidateId(), request.getFileName(), request.getFileUrl(), Instant.now());
        return mapper.toResponse(repository.save(resume));
    }

    @Override
    public ResumeResponse update(Long id, ResumeRequest request) {
        Resume resume = repository.findById(id);
        resume.setCandidateId(request.getCandidateId());
        resume.setFileName(request.getFileName());
        resume.setFileUrl(request.getFileUrl());
        return mapper.toResponse(repository.save(resume));
    }

    @Override
    @Transactional(readOnly = true)
    public ResumeResponse findById(Long id) {
        return mapper.toResponse(repository.findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResumeResponse> findAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResumeResponse> findByCandidate(Long candidateId) {
        return repository.findByCandidate(candidateId).stream().map(mapper::toResponse).toList();
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
