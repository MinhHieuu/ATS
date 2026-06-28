package com.example.ats.application.port.out;

import com.example.ats.domain.model.ActivityLog;

import java.util.List;

public interface ActivityLogRepository {
    ActivityLog save(ActivityLog log);
    ActivityLog findById(Long id);
    List<ActivityLog> findAll();
    List<ActivityLog> findByUser(Long userId);
    void deleteById(Long id);
}
