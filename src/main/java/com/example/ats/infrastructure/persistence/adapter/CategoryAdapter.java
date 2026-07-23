package com.example.ats.infrastructure.persistence.adapter;

import com.example.ats.application.mapper.CategoryMapper;
import com.example.ats.application.port.out.CategoryRepository;
import com.example.ats.domain.exception.ResourceNotFoundException;
import com.example.ats.domain.model.Category;
import com.example.ats.infrastructure.persistence.entity.CategoryEntity;
import com.example.ats.infrastructure.persistence.repository.SpringDataCategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryAdapter implements CategoryRepository {
    private final SpringDataCategoryRepository repository;
    private final CategoryMapper mapper;

    public CategoryAdapter(SpringDataCategoryRepository repository, CategoryMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Category save(Category category) {
        return mapper.toEntity(repository.save(new CategoryEntity(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getCreatedAt(),
                category.getUpdatedAt(),
                category.getIsActive())));
    }

    @Override
    public Category findById(Long id) {
        return mapper.toEntity(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found")));
    }

    @Override
    public Page<Category> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toEntity);
    }

    @Override
    public Page<Category> findActive(Pageable pageable) {
        return repository.findByIsActiveTrue(pageable).map(mapper::toEntity);
    }

    @Override
    public Page<Category> searchByNameAndActive(String name, Pageable pageable) {
        return repository.findByNameContainingIgnoreCaseAndIsActiveTrue(name, pageable).map(mapper::toEntity);
    }

    @Override
    public boolean existsByName(String name) {
        return repository.existsByNameIgnoreCase(name);
    }
}
