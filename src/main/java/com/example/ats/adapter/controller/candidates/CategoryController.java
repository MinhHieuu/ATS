package com.example.ats.adapter.controller.candidates;

import com.example.ats.application.dto.response.CategoryResponse;
import com.example.ats.application.port.in.CategoryUseCase;
import com.example.ats.domain.model.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryUseCase categoryUseCase;

    public CategoryController(CategoryUseCase categoryUseCase) {
        this.categoryUseCase = categoryUseCase;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CategoryResponse>>> findAll(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse<>("success", categoryUseCase.findActive(pageable)));
    }

    @GetMapping("search")
    public ResponseEntity<ApiResponse<Page<CategoryResponse>>> searchByName(
            @RequestParam String name,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse<>("success", categoryUseCase.searchActiveByName(name, pageable)));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>("success", categoryUseCase.findActiveById(id)));
    }
}
