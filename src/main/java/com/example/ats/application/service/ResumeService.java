package com.example.ats.application.service;

import com.example.ats.application.dto.request.ResumeRequest;
import com.example.ats.application.dto.response.ResumeResponse;
import com.example.ats.application.mapper.ResumeMapper;
import com.example.ats.application.port.in.ResumeUseCase;
import com.example.ats.application.port.out.ResumeRepository;
import com.example.ats.domain.model.Resume;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

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
    public Page<ResumeResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ResumeResponse> findByCandidate(Long candidateId, Pageable pageable) {
        return repository.findByCandidate(candidateId, pageable).map(mapper::toResponse);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
