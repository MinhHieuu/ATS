package com.example.ats.application.port.in;

import com.example.ats.application.dto.request.CompanyRequest;
import com.example.ats.application.dto.response.CompanyResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanyUseCase {
    CompanyResponse create(CompanyRequest request);
    CompanyResponse update(CompanyRequest request, Long id);
    CompanyResponse deactivate(Long id);
    CompanyResponse activate(Long id);
    CompanyResponse findById(Long id);
    CompanyResponse findActiveById(Long id);
    Page<CompanyResponse> findAll(Pageable pageable);
    Page<CompanyResponse> findActive(Pageable pageable);
    Page<CompanyResponse> searchActiveByName(String name, Pageable pageable);
}
