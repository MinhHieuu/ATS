package com.example.ats.infrastructure.persistence.repository;

import com.example.ats.domain.model.JobStatus;
import com.example.ats.infrastructure.persistence.entity.JobEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SpringDataJobRepository extends JpaRepository<JobEntity, Long> {
    // JobAdapter.toView() doc cac field cua company va category nen proxy luon phai khoi tao.
    // Dung EntityGraph de vua fetch san chung, vua ket hop duoc voi Pageable
    // (join fetch trong @Query se roi khi ket hop firstResult/maxResults).

    @Override
    @EntityGraph(attributePaths = {"company", "category"})
    Page<JobEntity> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"company", "category"})
    Optional<JobEntity> findById(Long id);

    @EntityGraph(attributePaths = {"company", "category"})
    Page<JobEntity> findByStatus(JobStatus status, Pageable pageable);

    @EntityGraph(attributePaths = {"company", "category"})
    Page<JobEntity> findByStatusNot(JobStatus status, Pageable pageable);

    @EntityGraph(attributePaths = {"company", "category"})
    @Query(value = "select j from JobEntity j where j.createdBy.id = :createdById",
            countQuery = "select count(j) from JobEntity j where j.createdBy.id = :createdById")
    Page<JobEntity> findByCreatedBy_Id(@Param("createdById") Long createdById, Pageable pageable);

    @EntityGraph(attributePaths = {"company", "category"})
    Page<JobEntity> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    @EntityGraph(attributePaths = {"company", "category"})
    Page<JobEntity> findByTitleContainingIgnoreCaseAndStatusNot(String title, JobStatus status, Pageable pageable);

    @EntityGraph(attributePaths = {"company", "category"})
    @Query(value = "select j from JobEntity j "
            + "where lower(j.title) like lower(concat(:title, '%')) and j.createdBy.id = :createdById",
            countQuery = "select count(j) from JobEntity j "
            + "where lower(j.title) like lower(concat(:title, '%')) and j.createdBy.id = :createdById")
    Page<JobEntity> findByTitleContainingIgnoreCaseAndCreatedBy_Id(@Param("title") String title,
                                                                   @Param("createdById") Long createdById,
                                                                   Pageable pageable);

    @EntityGraph(attributePaths = {"company", "category"})
    Page<JobEntity> findByCategory_Id(Long categoryId, Pageable pageable);

    @EntityGraph(attributePaths = {"company", "category"})
    Page<JobEntity> findByCategory_IdAndStatusNot(Long categoryId, JobStatus status, Pageable pageable);

    @EntityGraph(attributePaths = {"company", "category"})
    @Query(value = "select j from JobEntity j where j.category.id = :categoryId and j.createdBy.id = :createdById",
            countQuery = "select count(j) from JobEntity j where j.category.id = :categoryId and j.createdBy.id = :createdById")
    Page<JobEntity> findByCategory_IdAndCreatedBy_Id(@Param("categoryId") Long categoryId,
                                                      @Param("createdById") Long createdById,
                                                      Pageable pageable);
}
