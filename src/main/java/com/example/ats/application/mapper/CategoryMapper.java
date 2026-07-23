package com.example.ats.application.mapper;

import com.example.ats.application.dto.response.CategoryResponse;
import com.example.ats.domain.model.Category;
import com.example.ats.infrastructure.persistence.entity.CategoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponse toResponse(Category category);
    Category toEntity(CategoryEntity entity);
}
