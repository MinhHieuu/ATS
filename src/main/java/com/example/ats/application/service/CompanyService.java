package com.example.ats.application.service;

import com.example.ats.application.dto.request.CompanyRequest;
import com.example.ats.application.dto.response.CompanyResponse;
import com.example.ats.application.mapper.CompanyMapper;
import com.example.ats.application.port.in.CompanyUseCase;
import com.example.ats.application.port.out.CompanyRepository;
import com.example.ats.domain.model.Company;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class CompanyService implements CompanyUseCase {
    private final CompanyRepository repository;
    private final CompanyMapper mapper;

    public CompanyService(CompanyRepository repository, CompanyMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public CompanyResponse create(CompanyRequest request) {
        Instant now = Instant.now();
        Company company = repository.save(new Company(
                null,
                request.getName(),
                request.getLogo(),
                request.getEmail(),
                request.getWebsite(),
                request.getDescription(),
                request.getAddress(),
                now,
                null,
                true));
        return mapper.toResponse(company);
    }

    @Override
    public CompanyResponse update(CompanyRequest request, Long id) {
        Company company = repository.findById(id);
        company.setName(request.getName());
        company.setLogo(request.getLogo());
        company.setEmail(request.getEmail());
        company.setWebsite(request.getWebsite());
        company.setDescription(request.getDescription());
        company.setAddress(request.getAddress());
        company.setUpdatedAt(Instant.now());
        return mapper.toResponse(repository.save(company));
    }

    @Override
    public CompanyResponse deactivate(Long id) {
        Company company = repository.findById(id);
        company.setIsActive(false);
        company.setUpdatedAt(Instant.now());
        return mapper.toResponse(repository.save(company));
    }

    @Override
    public CompanyResponse findById(Long id) {
        return mapper.toResponse(repository.findById(id));
    }

    @Override
    public List<CompanyResponse> findAll() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyResponse> findActive() {
        return repository.findActive().stream()
                .map(mapper::toResponse)
                .toList();
    }
}
