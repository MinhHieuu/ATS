package com.example.ats.application.port.out;

import com.example.ats.domain.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryRepository {
    Category save(Category category);
    Category findById(Long id);
    Page<Category> findAll(Pageable pageable);
    Page<Category> findActive(Pageable pageable);
    Page<Category> searchByNameAndActive(String name, Pageable pageable);
    boolean existsByName(String name);
}
