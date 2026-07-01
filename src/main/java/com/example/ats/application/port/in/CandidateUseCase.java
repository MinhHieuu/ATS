package com.example.ats.application.port.in;

import com.example.ats.application.dto.request.CandidateRequest;
import com.example.ats.application.dto.response.CandidateResponse;

import java.util.List;

public interface CandidateUseCase {
    CandidateResponse create(CandidateRequest request);
    CandidateResponse update(CandidateRequest request, Long id);
    CandidateResponse updateByUserId(CandidateRequest request, Long userId);
    CandidateResponse findById(Long id);
    CandidateResponse findByUserId(Long userId);
    List<CandidateResponse> findAll();
}
