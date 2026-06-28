package com.example.ats.application.port.in;

import com.example.ats.application.dto.request.ActivityLogRequest;
import com.example.ats.application.dto.response.ActivityLogResponse;

import java.util.List;

public interface ActivityLogUseCase {
    ActivityLogResponse create(ActivityLogRequest request);
    ActivityLogResponse findById(Long id);
    List<ActivityLogResponse> findAll();
    List<ActivityLogResponse> findByUser(Long userId);
    void delete(Long id);
}
