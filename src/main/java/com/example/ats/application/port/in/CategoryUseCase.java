package com.example.ats.application.port.in;

import com.example.ats.application.dto.request.CategoryRequest;
import com.example.ats.application.dto.response.CategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryUseCase {
    CategoryResponse create(CategoryRequest request);
    CategoryResponse update(Long id, CategoryRequest request);
    CategoryResponse activate(Long id);
    CategoryResponse deactivate(Long id);
    CategoryResponse findById(Long id);
    CategoryResponse findActiveById(Long id);
    Page<CategoryResponse> findAll(Pageable pageable);
    Page<CategoryResponse> findActive(Pageable pageable);
    Page<CategoryResponse> searchActiveByName(String name, Pageable pageable);
}
