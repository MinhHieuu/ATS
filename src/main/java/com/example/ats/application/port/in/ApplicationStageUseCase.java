package com.example.ats.application.port.in;

import com.example.ats.application.dto.request.ApplicationStageRequest;
import com.example.ats.application.dto.response.ApplicationStageResponse;

import java.util.List;

public interface ApplicationStageUseCase {
    ApplicationStageResponse create(ApplicationStageRequest request);
    ApplicationStageResponse update(Long id, ApplicationStageRequest request);
    ApplicationStageResponse findById(Long id);
    List<ApplicationStageResponse> findAll();
    void delete(Long id);
}
