package com.example.ats.application.port.out;

import com.example.ats.domain.model.Company;

import java.util.List;

public interface CompanyRepository {
    Company save(Company company);
    Company findById(Long id);
    List<Company> findAll();
}
