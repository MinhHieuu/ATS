package com.example.ats.adapter.controller.admin;

import com.example.ats.application.dto.request.CompanyRequest;
import com.example.ats.application.dto.response.CompanyResponse;
import com.example.ats.application.port.in.CompanyUseCase;
import com.example.ats.domain.model.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/companies")
public class AdminCompanyController {
    private final CompanyUseCase companyUseCase;

    public AdminCompanyController(CompanyUseCase companyUseCase) {
        this.companyUseCase = companyUseCase;
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<CompanyResponse>> create(@Valid @RequestBody CompanyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("create success", companyUseCase.create(request)));
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<CompanyResponse>>> findAll() {
        return ResponseEntity.ok(new ApiResponse<>("success", companyUseCase.findAll()));
    }

    @GetMapping("active")
    public ResponseEntity<ApiResponse<List<CompanyResponse>>> findActive() {
        return ResponseEntity.ok(new ApiResponse<>("success", companyUseCase.findActive()));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<CompanyResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>("success", companyUseCase.findById(id)));
    }

    @PatchMapping("{id}")
    public ResponseEntity<ApiResponse<CompanyResponse>> update(@PathVariable Long id,
                                                               @Valid @RequestBody CompanyRequest request) {
        return ResponseEntity.ok(new ApiResponse<>("success", companyUseCase.update(request, id)));
    }

    @PatchMapping("{id}/deactivate")
    public ResponseEntity<ApiResponse<CompanyResponse>> deactivate(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>("success", companyUseCase.deactivate(id)));
    }

    @PatchMapping("{id}/active")
    public ResponseEntity<ApiResponse<CompanyResponse>> active(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>("success", companyUseCase.activate(id)));
    }
}
