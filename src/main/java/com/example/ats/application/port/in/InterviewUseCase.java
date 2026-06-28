package com.example.ats.application.port.in;

import com.example.ats.application.dto.request.InterviewRequest;
import com.example.ats.application.dto.request.InterviewStatusRequest;
import com.example.ats.application.dto.response.InterviewResponse;

import java.util.List;

public interface InterviewUseCase {
    InterviewResponse create(InterviewRequest request);
    InterviewResponse update(Long id, InterviewRequest request);
    InterviewResponse changeStatus(Long id, InterviewStatusRequest request);
    InterviewResponse findById(Long id);
    List<InterviewResponse> findAll();
    List<InterviewResponse> findByApplication(Long applicationId);
    void delete(Long id);
}
