package com.example.ats.application.service;

import com.example.ats.application.dto.request.CandidateRequest;
import com.example.ats.application.dto.response.CandidateResponse;
import com.example.ats.application.dto.response.UserResponse;
import com.example.ats.application.mapper.CandidateMapper;
import com.example.ats.application.mapper.UserMapper;
import com.example.ats.application.port.in.AuditLogUseCase;
import com.example.ats.application.port.in.CandidateUseCase;
import com.example.ats.application.port.in.UserUseCase;
import com.example.ats.application.port.out.CandidateRepository;
import com.example.ats.domain.model.AuditAction;
import com.example.ats.domain.model.AuditEntityType;
import com.example.ats.domain.model.Candidate;
import com.example.ats.domain.model.Role;
import com.example.ats.domain.view.CandidateView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Transactional
public class CandidateService implements CandidateUseCase {
    private final CandidateRepository repository;
    private final UserUseCase userUseCase;
    private final CandidateMapper mapper;
    private final UserMapper userMapper;
    private final AuditLogUseCase auditLog;

    public CandidateService(CandidateRepository repository, UserUseCase userUseCase,
                            CandidateMapper mapper, UserMapper userMapper, AuditLogUseCase auditLog) {
        this.repository = repository;
        this.userUseCase = userUseCase;
        this.mapper = mapper;
        this.userMapper = userMapper;
        this.auditLog = auditLog;
    }

    @Override
    public CandidateResponse create(CandidateRequest request) {
        UserResponse user = userUseCase.create(request, Role.CANDIDATE);
        Instant now = Instant.now();
        Candidate candidate = repository.save(
                new Candidate(null, user.getId(), request.getLinkedinUrl(), request.getGithubUrl(),
                        request.getPortfolioUrl(), request.getCurrentPosition(),
                        request.getYearsOfExperience(), now, null)
        );
        CandidateResponse response = mapper.toResponse(candidate);
        if(user != null) response.setUser(user);
        auditLog.log(AuditAction.CANDIDATE_CREATED, AuditEntityType.CANDIDATE, candidate.getId(),
                "Tạo hồ sơ ứng viên cho tài khoản " + (user != null ? user.getEmail() : "?"));
        return response;
    }

    @Override
    public CandidateResponse update(CandidateRequest request, Long id) {
        Instant now = Instant.now();
        Candidate candidate = repository.findById(id);
        UserResponse user = userUseCase.update(candidate.getUserId(), request);
        return updateCandidate(request, user, now, candidate);
    }

    private CandidateResponse updateCandidate(CandidateRequest request, UserResponse user, Instant now, Candidate candidate) {
        candidate.setLinkedinUrl(request.getLinkedinUrl());
        candidate.setGithubUrl(request.getGithubUrl());
        candidate.setPortfolioUrl(request.getPortfolioUrl());
        candidate.setCurrentPosition(request.getCurrentPosition());
        candidate.setYearsOfExperience(request.getYearsOfExperience());
        candidate.setUpdatedAt(now);
        repository.save(candidate);
        CandidateResponse response = mapper.toResponse(candidate);
        if(user != null) response.setUser(user);
        auditLog.log(AuditAction.CANDIDATE_UPDATED, AuditEntityType.CANDIDATE, candidate.getId(),
                "Cập nhật hồ sơ ứng viên #" + candidate.getId());
        return response;
    }

    @Override
    public CandidateResponse findById(Long id) {
        return toResponse(repository.findById(id));
    }

    @Override
    public Page<CandidateResponse> findAll(Pageable pageable) {
        return repository.findAllWithUser(pageable).map(this::toResponse);
    }

      private CandidateResponse toResponse(CandidateView candidateWithUser) {
        CandidateResponse response = mapper.toResponse(candidateWithUser.candidate());
        response.setUser(userMapper.toResponse(candidateWithUser.user()));
        return response;
    }

    private CandidateResponse toResponse(Candidate entity) {
        CandidateResponse response = mapper.toResponse(entity);
        response.setUser(userUseCase.findById(entity.getUserId()));
        return response;
    }
}
