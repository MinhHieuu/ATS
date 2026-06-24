package com.example.ats.infrastructure.persistence.repository;

import com.example.ats.infrastructure.persistence.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataCompanyRepository extends JpaRepository<CompanyEntity, Long> {
}
