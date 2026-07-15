package com.example.ats.application.port.in;

import com.example.ats.application.dto.request.CompanyRequest;
import com.example.ats.application.dto.response.CompanyResponse;

import java.util.List;

public interface CompanyUseCase {
    CompanyResponse create(CompanyRequest request);
    CompanyResponse update(CompanyRequest request, Long id);
    CompanyResponse deactivate(Long id);
    CompanyResponse activate(Long id);
    CompanyResponse findById(Long id);
    List<CompanyResponse> findAll();
    List<CompanyResponse> findActive();
}
