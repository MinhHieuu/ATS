package com.example.ats.infrastructure.persistence.repository;

import com.example.ats.infrastructure.persistence.entity.ActivityLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SpringDataActivityLogRepository extends JpaRepository<ActivityLogEntity, Long> {
    List<ActivityLogEntity> findByUser_IdOrderByCreatedAtDesc(Long userId);
}
