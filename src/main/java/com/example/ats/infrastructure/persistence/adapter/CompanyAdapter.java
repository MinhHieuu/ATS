package com.example.ats.infrastructure.persistence.adapter;

import com.example.ats.application.mapper.CompanyMapper;
import com.example.ats.application.port.out.CompanyRepository;
import com.example.ats.domain.exception.ResourceNotFoundException;
import com.example.ats.domain.model.Company;
import com.example.ats.infrastructure.persistence.entity.CompanyEntity;
import com.example.ats.infrastructure.persistence.repository.SpringDataCompanyRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CompanyAdapter implements CompanyRepository {
    private final SpringDataCompanyRepository repository;
    private final CompanyMapper mapper;

    public CompanyAdapter(SpringDataCompanyRepository repository, CompanyMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Company save(Company company) {
        return mapper.toEntity(repository.save(new CompanyEntity(
                company.getId(),
                company.getName(),
                company.getLogo(),
                company.getEmail(),
                company.getWebsite(),
                company.getDescription(),
                company.getAddress(),
                company.getCreatedAt(),
                company.getUpdatedAt())));
    }

    @Override
    public Company findById(Long id) {
        return mapper.toEntity(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found")));
    }

    @Override
    public List<Company> findAll() {
        return repository.findAll().stream()
                .map(mapper::toEntity)
                .toList();
    }
}
