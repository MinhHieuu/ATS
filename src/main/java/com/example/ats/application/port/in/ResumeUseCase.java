package com.example.ats.application.port.in;

import com.example.ats.application.dto.request.ResumeRequest;
import com.example.ats.application.dto.response.ResumeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ResumeUseCase {
    ResumeResponse create(ResumeRequest request);
    ResumeResponse update(Long id, ResumeRequest request);
    ResumeResponse findById(Long id);
    Page<ResumeResponse> findAll(Pageable pageable);
    Page<ResumeResponse> findByCandidate(Long candidateId, Pageable pageable);
    void delete(Long id);
}
