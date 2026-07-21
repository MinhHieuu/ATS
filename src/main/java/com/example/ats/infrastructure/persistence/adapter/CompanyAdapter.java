package com.example.ats.infrastructure.persistence.adapter;

import com.example.ats.application.mapper.CompanyMapper;
import com.example.ats.application.port.out.CompanyRepository;
import com.example.ats.domain.exception.ResourceNotFoundException;
import com.example.ats.domain.model.Company;
import com.example.ats.infrastructure.persistence.entity.CompanyEntity;
import com.example.ats.infrastructure.persistence.repository.SpringDataCompanyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

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
                company.getUpdatedAt(),
                company.getIsActive())));
    }

    @Override
    public Company findById(Long id) {
        return mapper.toEntity(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found")));
    }

    @Override
    public Page<Company> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toEntity);
    }

    @Override
    public Page<Company> findActive(Pageable pageable) {
        return repository.findByIsActiveTrue(pageable).map(mapper::toEntity);
    }

    @Override
    public Page<Company> searchByNameAndActive(String name, Pageable pageable) {
        return repository.findByNameContainingIgnoreCaseAndIsActiveTrue(name, pageable).map(mapper::toEntity);
    }
}
