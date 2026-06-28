package com.example.ats.application.port.in;

import com.example.ats.application.dto.request.ResumeRequest;
import com.example.ats.application.dto.response.ResumeResponse;

import java.util.List;

public interface ResumeUseCase {
    ResumeResponse create(ResumeRequest request);
    ResumeResponse update(Long id, ResumeRequest request);
    ResumeResponse findById(Long id);
    List<ResumeResponse> findAll();
    List<ResumeResponse> findByCandidate(Long candidateId);
    void delete(Long id);
}
