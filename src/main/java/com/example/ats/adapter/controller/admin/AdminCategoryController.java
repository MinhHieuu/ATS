package com.example.ats.adapter.controller.admin;

import com.example.ats.application.dto.request.CategoryRequest;
import com.example.ats.application.dto.response.CategoryResponse;
import com.example.ats.application.port.in.CategoryUseCase;
import com.example.ats.domain.model.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/categories")
public class AdminCategoryController {
    private final CategoryUseCase categoryUseCase;

    public AdminCategoryController(CategoryUseCase categoryUseCase) {
        this.categoryUseCase = categoryUseCase;
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<CategoryResponse>> create(@Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("create success", categoryUseCase.create(request)));
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<Page<CategoryResponse>>> findAll(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse<>("success", categoryUseCase.findAll(pageable)));
    }

    @GetMapping("active")
    public ResponseEntity<ApiResponse<Page<CategoryResponse>>> findActive(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse<>("success", categoryUseCase.findActive(pageable)));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>("success", categoryUseCase.findById(id)));
    }

    @PatchMapping("{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> update(@PathVariable Long id,
                                                                @Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(new ApiResponse<>("success", categoryUseCase.update(id, request)));
    }

    @PatchMapping("{id}/deactivate")
    public ResponseEntity<ApiResponse<CategoryResponse>> deactivate(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>("success", categoryUseCase.deactivate(id)));
    }

    @PatchMapping("{id}/active")
    public ResponseEntity<ApiResponse<CategoryResponse>> active(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>("success", categoryUseCase.activate(id)));
    }
}
