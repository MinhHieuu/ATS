package com.example.ats.infrastructure.persistence.repository;

import com.example.ats.infrastructure.persistence.entity.InterviewFeedbackEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SpringDataInterviewFeedbackRepository extends JpaRepository<InterviewFeedbackEntity, Long> {

    @Override
    @EntityGraph(attributePaths = {"reviewer", "interview", "interview.application",
            "interview.application.candidate", "interview.application.candidate.user", "interview.application.job"})
    Page<InterviewFeedbackEntity> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"reviewer", "interview", "interview.application",
            "interview.application.candidate", "interview.application.candidate.user", "interview.application.job"})
    Optional<InterviewFeedbackEntity> findById(Long id);

    @EntityGraph(attributePaths = {"reviewer", "interview", "interview.application",
            "interview.application.candidate", "interview.application.candidate.user", "interview.application.job"})
    Page<InterviewFeedbackEntity> findByInterview_Id(Long interviewId, Pageable pageable);

    @EntityGraph(attributePaths = {"reviewer", "interview", "interview.application",
            "interview.application.candidate", "interview.application.candidate.user", "interview.application.job"})
    @Query(value = "select f from InterviewFeedbackEntity f where f.interview.application.job.createdBy.id = :recruiterId",
            countQuery = "select count(f) from InterviewFeedbackEntity f where f.interview.application.job.createdBy.id = :recruiterId")
    Page<InterviewFeedbackEntity> findByJobCreatedBy(@Param("recruiterId") Long recruiterId, Pageable pageable);

    @EntityGraph(attributePaths = {"reviewer", "interview", "interview.application",
            "interview.application.candidate", "interview.application.candidate.user", "interview.application.job"})
    @Query("select f from InterviewFeedbackEntity f where f.id = :id and f.interview.application.job.createdBy.id = :recruiterId")
    Optional<InterviewFeedbackEntity> findByIdAndJobCreatedBy(@Param("id") Long id,
                                                              @Param("recruiterId") Long recruiterId);

    @EntityGraph(attributePaths = {"reviewer", "interview", "interview.application",
            "interview.application.candidate", "interview.application.candidate.user", "interview.application.job"})
    @Query(value = "select f from InterviewFeedbackEntity f where f.interview.id = :interviewId and f.interview.application.job.createdBy.id = :recruiterId",
            countQuery = "select count(f) from InterviewFeedbackEntity f where f.interview.id = :interviewId and f.interview.application.job.createdBy.id = :recruiterId")
    Page<InterviewFeedbackEntity> findByInterviewAndJobCreatedBy(@Param("interviewId") Long interviewId,
                                                                 @Param("recruiterId") Long recruiterId,
                                                                 Pageable pageable);

    boolean existsByInterview_IdAndReviewer_Id(Long interviewId, Long reviewerId);
}
