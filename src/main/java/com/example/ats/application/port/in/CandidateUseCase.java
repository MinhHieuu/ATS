package com.example.ats.application.port.in;

import com.example.ats.application.dto.request.CandidateRequest;
import com.example.ats.application.dto.response.CandidateResponse;

import java.util.List;

public interface CandidateUseCase {
    CandidateResponse create(CandidateRequest request);
    CandidateResponse update(CandidateRequest request, Long id);
    CandidateResponse findById(Long id);
    List<CandidateResponse> findAll();
}
