package com.example.ats.infrastructure.persistence.adapter;

import com.example.ats.application.mapper.CompanyMapper;
import com.example.ats.application.mapper.RecruiterMapper;
import com.example.ats.application.mapper.UserMapper;
import com.example.ats.application.port.out.RecruiterRepository;
import com.example.ats.domain.exception.ResourceNotFoundException;
import com.example.ats.domain.model.Company;
import com.example.ats.domain.model.Recruiter;
import com.example.ats.domain.model.User;
import com.example.ats.domain.result.RecruiterResult;
import com.example.ats.infrastructure.persistence.entity.CompanyEntity;
import com.example.ats.infrastructure.persistence.entity.RecruiterEntity;
import com.example.ats.infrastructure.persistence.entity.UserEntity;
import com.example.ats.infrastructure.persistence.repository.SpringDataCompanyRepository;
import com.example.ats.infrastructure.persistence.repository.SpringDataRecruiterRepository;
import com.example.ats.infrastructure.persistence.repository.SpringDataUserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RecruiterAdapter implements RecruiterRepository {
    private final SpringDataRecruiterRepository repository;
    private final SpringDataUserRepository userRepository;
    private final SpringDataCompanyRepository companyRepository;
    private final RecruiterMapper mapper;
    private final UserMapper userMapper;
    private final CompanyMapper companyMapper;

    public RecruiterAdapter(SpringDataRecruiterRepository repository, SpringDataUserRepository userRepository,
                            SpringDataCompanyRepository companyRepository, RecruiterMapper mapper,
                            UserMapper userMapper, CompanyMapper companyMapper) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.mapper = mapper;
        this.userMapper = userMapper;
        this.companyMapper = companyMapper;
    }

    @Override
    public Recruiter save(Recruiter recruiter) {
        UserEntity user = userRepository.findById(recruiter.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + recruiter.getUserId()));
        CompanyEntity company = null;
        if (recruiter.getCompanyId() != null) {
            company = companyRepository.findById(recruiter.getCompanyId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Company not found with id: " + recruiter.getCompanyId()));
        }
        return toRecruiter(repository.save(new RecruiterEntity(
                recruiter.getId(), user, company, recruiter.getPosition())));
    }

    @Override
    public Recruiter findById(Long id) {
        return toRecruiter(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter not found")));
    }

    @Override
    public Recruiter findByUserId(Long userId) {
        return toRecruiter(repository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter not found with user id: " + userId)));
    }

    @Override
    public List<RecruiterResult> findAllWithUser() {
        return repository.findAllWithUser().stream()
                .map(entity -> new RecruiterResult(
                        toRecruiter(entity),
                        toUser(entity.getUser()),
                        toCompany(entity.getCompany())))
                .toList();
    }

    private Recruiter toRecruiter(RecruiterEntity entity) {
        Recruiter recruiter = mapper.toEntity(entity);
        recruiter.setUserId(entity.getUser().getId());
        if (entity.getCompany() != null) {
            recruiter.setCompanyId(entity.getCompany().getId());
        }
        return recruiter;
    }

    private User toUser(UserEntity entity) {
        return userMapper.toEntity(entity);
    }

    private Company toCompany(CompanyEntity entity) {
        if (entity == null) {
            return null;
        }
        return companyMapper.toEntity(entity);
    }
}
