package com.example.ats.infrastructure.persistence.repository;

import com.example.ats.infrastructure.persistence.entity.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataCategoryRepository extends JpaRepository<CategoryEntity, Long> {
    Page<CategoryEntity> findByIsActiveTrue(Pageable pageable);
    Page<CategoryEntity> findByNameContainingIgnoreCaseAndIsActiveTrue(String name, Pageable pageable);
    boolean existsByNameIgnoreCase(String name);
}
