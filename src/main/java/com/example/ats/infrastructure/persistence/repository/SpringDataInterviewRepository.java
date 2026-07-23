package com.example.ats.infrastructure.persistence.repository;

import com.example.ats.domain.model.InterviewResult;
import com.example.ats.infrastructure.persistence.entity.InterviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SpringDataInterviewRepository extends JpaRepository<InterviewEntity, Long> {

    @Override
    @EntityGraph(attributePaths = {"application", "application.candidate", "application.candidate.user", "application.job"})
    Page<InterviewEntity> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"application", "application.candidate", "application.candidate.user", "application.job"})
    Optional<InterviewEntity> findById(Long id);

    @EntityGraph(attributePaths = {"application", "application.candidate", "application.candidate.user", "application.job"})
    Page<InterviewEntity> findByApplication_Id(Long applicationId, Pageable pageable);

    @EntityGraph(attributePaths = {"application", "application.candidate", "application.candidate.user", "application.job"})
    @Query(value = "select i from InterviewEntity i where i.application.job.createdBy.id = :recruiterId",
            countQuery = "select count(i) from InterviewEntity i where i.application.job.createdBy.id = :recruiterId")
    Page<InterviewEntity> findByJobCreatedBy(@Param("recruiterId") Long recruiterId, Pageable pageable);

    @EntityGraph(attributePaths = {"application", "application.candidate", "application.candidate.user", "application.job"})
    @Query("select i from InterviewEntity i where i.id = :id and i.application.job.createdBy.id = :recruiterId")
    Optional<InterviewEntity> findByIdAndJobCreatedBy(@Param("id") Long id,
                                                       @Param("recruiterId") Long recruiterId);

    @EntityGraph(attributePaths = {"application", "application.candidate", "application.candidate.user", "application.job"})
    @Query(value = "select i from InterviewEntity i where i.application.id = :applicationId and i.application.job.createdBy.id = :recruiterId",
            countQuery = "select count(i) from InterviewEntity i where i.application.id = :applicationId and i.application.job.createdBy.id = :recruiterId")
    Page<InterviewEntity> findByApplicationAndJobCreatedBy(@Param("applicationId") Long applicationId,
                                                            @Param("recruiterId") Long recruiterId,
                                                            Pageable pageable);

    @EntityGraph(attributePaths = {"application", "application.candidate", "application.candidate.user", "application.job"})
    @Query(value = "select i from InterviewEntity i where i.application.candidate.id = :candidateId",
            countQuery = "select count(i) from InterviewEntity i where i.application.candidate.id = :candidateId")
    Page<InterviewEntity> findByCandidate(@Param("candidateId") Long candidateId, Pageable pageable);

    @EntityGraph(attributePaths = {"application", "application.candidate", "application.candidate.user", "application.job"})
    @Query("select i from InterviewEntity i where i.id = :id and i.application.candidate.id = :candidateId")
    Optional<InterviewEntity> findByIdAndCandidate(@Param("id") Long id,
                                                    @Param("candidateId") Long candidateId);

    boolean existsByApplication_IdAndResult(Long applicationId, InterviewResult result);
}
