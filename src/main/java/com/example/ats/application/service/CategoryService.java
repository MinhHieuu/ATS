package com.example.ats.application.service;

import com.example.ats.application.dto.request.CategoryRequest;
import com.example.ats.application.dto.response.CategoryResponse;
import com.example.ats.application.mapper.CategoryMapper;
import com.example.ats.application.port.in.CategoryUseCase;
import com.example.ats.application.port.out.CategoryRepository;
import com.example.ats.domain.exception.BusinessRuleException;
import com.example.ats.domain.exception.ResourceNotFoundException;
import com.example.ats.domain.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Transactional
public class CategoryService implements CategoryUseCase {
    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    public CategoryService(CategoryRepository repository, CategoryMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public CategoryResponse create(CategoryRequest request) {
        if (repository.existsByName(request.getName())) {
            throw new BusinessRuleException("Category name already exists");
        }
        Category category = repository.save(new Category(
                null, request.getName(), request.getDescription(), Instant.now(), null, true));
        return mapper.toResponse(category);
    }

    @Override
    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = repository.findById(id);
        if (!category.getName().equalsIgnoreCase(request.getName())
                && repository.existsByName(request.getName())) {
            throw new BusinessRuleException("Category name already exists");
        }
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setUpdatedAt(Instant.now());
        return mapper.toResponse(repository.save(category));
    }

    @Override
    public CategoryResponse activate(Long id) {
        return updateActive(id, true);
    }

    @Override
    public CategoryResponse deactivate(Long id) {
        return updateActive(id, false);
    }

    @Override
    public CategoryResponse findById(Long id) {
        return mapper.toResponse(repository.findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse findActiveById(Long id) {
        Category category = repository.findById(id);
        // Bao 404 thay vi 403 de khong lo ra su ton tai cua category da ngung hoat dong.
        if (!Boolean.TRUE.equals(category.getIsActive())) {
            throw new ResourceNotFoundException("Category not found");
        }
        return mapper.toResponse(category);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryResponse> findActive(Pageable pageable) {
        return repository.findActive(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryResponse> searchActiveByName(String name, Pageable pageable) {
        return repository.searchByNameAndActive(name, pageable).map(mapper::toResponse);
    }

    private CategoryResponse updateActive(Long id, boolean active) {
        Category category = repository.findById(id);
        category.setIsActive(active);
        category.setUpdatedAt(Instant.now());
        return mapper.toResponse(repository.save(category));
    }
}
