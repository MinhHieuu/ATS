package com.example.ats.application.service;

import com.example.ats.application.dto.request.RecruiterRequest;
import com.example.ats.application.dto.response.RecruiterResponse;
import com.example.ats.application.dto.response.UserResponse;
import com.example.ats.application.mapper.CompanyMapper;
import com.example.ats.application.mapper.RecruiterMapper;
import com.example.ats.application.mapper.UserMapper;
import com.example.ats.application.port.in.CompanyUseCase;
import com.example.ats.application.port.in.RecruiterUseCase;
import com.example.ats.application.port.in.UserUseCase;
import com.example.ats.application.port.out.RecruiterRepository;
import com.example.ats.domain.model.Recruiter;
import com.example.ats.domain.model.Role;
import com.example.ats.domain.view.RecruiterView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RecruiterService implements RecruiterUseCase {
    private final RecruiterRepository repository;
    private final UserUseCase userUseCase;
    private final CompanyUseCase companyUseCase;
    private final RecruiterMapper mapper;
    private final UserMapper userMapper;
    private final CompanyMapper companyMapper;

    public RecruiterService(RecruiterRepository repository, UserUseCase userUseCase,
                            CompanyUseCase companyUseCase, RecruiterMapper mapper,
                            UserMapper userMapper, CompanyMapper companyMapper) {
        this.repository = repository;
        this.userUseCase = userUseCase;
        this.companyUseCase = companyUseCase;
        this.mapper = mapper;
        this.userMapper = userMapper;
        this.companyMapper = companyMapper;
    }

    @Override
    public RecruiterResponse create(RecruiterRequest request) {
        UserResponse user = userUseCase.create(request, Role.RECRUITER);
        Recruiter recruiter = repository.save(new Recruiter(
                null, user.getId(), request.getCompanyId(), request.getPosition()));
        RecruiterResponse response = mapper.toResponse(recruiter);
        if (user != null) {
            response.setUser(user);
        }
        setCompany(response, recruiter);
        return response;
    }

    @Override
    public RecruiterResponse update(RecruiterRequest request, Long id) {
        Recruiter recruiter = repository.findById(id);
        UserResponse user = userUseCase.update(recruiter.getUserId(), request);
        recruiter.setCompanyId(request.getCompanyId());
        recruiter.setPosition(request.getPosition());
        repository.save(recruiter);
        RecruiterResponse response = mapper.toResponse(recruiter);
        if (user != null) {
            response.setUser(user);
        }
        setCompany(response, recruiter);
        return response;
    }

    @Override
    public RecruiterResponse findById(Long id) {
        return toResponse(repository.findById(id));
    }


    @Override
    public Page<RecruiterResponse> findAll(Pageable pageable) {
        return repository.findAllWithUser(pageable).map(this::toResponse);
    }

    private RecruiterResponse toResponse(RecruiterView recruiterWithUser) {
        RecruiterResponse response = mapper.toResponse(recruiterWithUser.recruiter());
        response.setUser(userMapper.toResponse(recruiterWithUser.user()));
        if (recruiterWithUser.company() != null) {
            response.setCompany(companyMapper.toResponse(recruiterWithUser.company()));
        }
        return response;
    }

    private RecruiterResponse toResponse(Recruiter recruiter) {
        RecruiterResponse response = mapper.toResponse(recruiter);
        response.setUser(userUseCase.findById(recruiter.getUserId()));
        setCompany(response, recruiter);
        return response;
    }

    private void setCompany(RecruiterResponse response, Recruiter recruiter) {
        if (recruiter.getCompanyId() != null) {
            response.setCompany(companyUseCase.findById(recruiter.getCompanyId()));
        }
    }
}
