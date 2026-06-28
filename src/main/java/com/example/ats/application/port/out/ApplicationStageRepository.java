package com.example.ats.application.port.out;

import com.example.ats.domain.model.ApplicationStage;

import java.util.List;

public interface ApplicationStageRepository {
    ApplicationStage save(ApplicationStage stage);
    ApplicationStage findById(Long id);
    List<ApplicationStage> findAll();
    void deleteById(Long id);
}
