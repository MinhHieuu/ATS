package com.example.ats.infrastructure.persistence.repository;

import com.example.ats.infrastructure.persistence.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataCompanyRepository extends JpaRepository<CompanyEntity, Long> {
    List<CompanyEntity> findByIsActiveTrue();
}
