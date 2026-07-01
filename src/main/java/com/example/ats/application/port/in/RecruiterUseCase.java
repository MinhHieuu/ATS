package com.example.ats.application.port.in;

import com.example.ats.application.dto.request.RecruiterRequest;
import com.example.ats.application.dto.response.RecruiterResponse;

import java.util.List;

public interface RecruiterUseCase {
    RecruiterResponse create(RecruiterRequest request);
    RecruiterResponse update(RecruiterRequest request, Long id);
    RecruiterResponse findById(Long id);
    RecruiterResponse findByUserId(Long userId);
    List<RecruiterResponse> findAll();
}
