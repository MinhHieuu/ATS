package com.example.ats.adapter.controller.candidates;

import com.example.ats.application.dto.response.CompanyResponse;
import com.example.ats.application.port.in.CompanyUseCase;
import com.example.ats.domain.model.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {
    private final CompanyUseCase companyUseCase;

    public CompanyController(CompanyUseCase companyUseCase) {
        this.companyUseCase = companyUseCase;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CompanyResponse>>> findAll(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse<>("success", companyUseCase.findActive(pageable)));
    }

    @GetMapping("search")
    public ResponseEntity<ApiResponse<Page<CompanyResponse>>> searchByName(
            @RequestParam String name,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse<>("success", companyUseCase.searchActiveByName(name, pageable)));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<CompanyResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>("success", companyUseCase.findActiveById(id)));
    }
}
