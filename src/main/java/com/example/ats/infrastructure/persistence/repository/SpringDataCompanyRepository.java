package com.example.ats.infrastructure.persistence.repository;

import com.example.ats.infrastructure.persistence.entity.CompanyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataCompanyRepository extends JpaRepository<CompanyEntity, Long> {
    Page<CompanyEntity> findByIsActiveTrue(Pageable pageable);

    Page<CompanyEntity> findByNameContainingIgnoreCaseAndIsActiveTrue(String name, Pageable pageable);
}
