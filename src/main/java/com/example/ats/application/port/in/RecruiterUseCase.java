package com.example.ats.application.port.in;

import com.example.ats.application.dto.request.RecruiterRequest;
import com.example.ats.application.dto.response.RecruiterResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecruiterUseCase {
    RecruiterResponse create(RecruiterRequest request);
    RecruiterResponse update(RecruiterRequest request, Long id);
    RecruiterResponse findById(Long id);
    Page<RecruiterResponse> findAll(Pageable pageable);
}
