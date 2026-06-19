package com.example.ats.infrastructure.persistence.repository;

import com.example.ats.infrastructure.persistence.entity.ApplicationStageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SpringDataApplicationStageRepository
        extends JpaRepository<ApplicationStageEntity, Long> {
    List<ApplicationStageEntity> findAllByOrderByPositionAsc();
}
