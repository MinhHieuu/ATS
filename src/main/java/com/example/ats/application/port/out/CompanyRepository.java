package com.example.ats.application.port.out;

import com.example.ats.domain.model.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanyRepository {
    Company save(Company company);
    Company findById(Long id);
    Page<Company> findAll(Pageable pageable);
    Page<Company> findActive(Pageable pageable);
    Page<Company> searchByNameAndActive(String name, Pageable pageable);
}
