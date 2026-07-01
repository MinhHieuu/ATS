package com.example.ats.infrastructure.persistence.adapter;

import com.example.ats.application.mapper.CandidateMapper;
import com.example.ats.application.mapper.UserMapper;
import com.example.ats.application.port.out.CandidateRepository;
import com.example.ats.domain.exception.ResourceNotFoundException;
import com.example.ats.domain.model.Candidate;
import com.example.ats.domain.model.User;
import com.example.ats.domain.result.CandidateResult;
import com.example.ats.infrastructure.persistence.entity.CandidateEntity;
import com.example.ats.infrastructure.persistence.entity.UserEntity;
import com.example.ats.infrastructure.persistence.repository.SpringDataCandidateRepository;
import com.example.ats.infrastructure.persistence.repository.SpringDataUserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CandidateAdapter implements CandidateRepository {
    private final SpringDataCandidateRepository repository;
    private final SpringDataUserRepository userRepository;
    private final CandidateMapper mapper;
    private final UserMapper userMapper;
    public CandidateAdapter(SpringDataCandidateRepository repository, SpringDataUserRepository userRepository,
                            CandidateMapper mapper, UserMapper userMapper) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.userMapper = userMapper;
    }

    @Override
    public Candidate save(Candidate candidate) {
        UserEntity user = userRepository.findById(candidate.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + candidate.getUserId()));
        return toCandidate(repository.save(new CandidateEntity(candidate.getId(), user, candidate.getLinkedinUrl(),
                candidate.getGithubUrl(), candidate.getPortfolioUrl(), candidate.getCurrentPosition(),
                candidate.getYearsOfExperience(), candidate.getCreatedAt(), candidate.getUpdatedAt())));
    }

    @Override
    public Candidate findById(Long id) {
        return toCandidate(repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Candidate not found")));
    }

    @Override
    public Candidate findByUserId(Long userId) {
        return toCandidate(repository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found")));
    }

    @Override
    public List<CandidateResult> findAllWithUser() {
        return repository.findAllWithUser().stream()
                .map(entity -> new CandidateResult(
                        toCandidate(entity),
                        toUser(entity.getUser())))
                .toList();
    }

    private Candidate toCandidate(CandidateEntity entity) {
        Candidate candidate = mapper.toEntity(entity);
        candidate.setUserId(entity.getUser().getId());
        return candidate;
    }

    private User toUser(UserEntity entity) {
        return userMapper.toEntity(entity);
    }
}


