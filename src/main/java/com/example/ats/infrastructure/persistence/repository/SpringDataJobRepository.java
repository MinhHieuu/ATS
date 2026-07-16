package com.example.ats.infrastructure.persistence.repository;

import com.example.ats.domain.model.JobStatus;
import com.example.ats.infrastructure.persistence.entity.JobEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SpringDataJobRepository extends JpaRepository<JobEntity, Long> {
    // JobAdapter.toView() doc cac field cua company nen proxy luon phai khoi tao.
    // Fetch san company de tranh N+1; giu createdBy lazy vi toView chi can getId().

    @Override
    @Query("select j from JobEntity j join fetch j.company")
    List<JobEntity> findAll();

    @Override
    @Query("select j from JobEntity j join fetch j.company where j.id = :id")
    Optional<JobEntity> findById(@Param("id") Long id);

    @Query("select j from JobEntity j join fetch j.company where j.status = :status")
    List<JobEntity> findByStatus(@Param("status") JobStatus status);

    @Query("select j from JobEntity j join fetch j.company where j.status <> :status")
    List<JobEntity> findByStatusNot(@Param("status") JobStatus status);

    @Query("select j from JobEntity j join fetch j.company where j.createdBy.id = :createdById")
    List<JobEntity> findByCreatedBy_Id(@Param("createdById") Long createdById);

    @Query("select j from JobEntity j join fetch j.company "
            + "where lower(j.title) like lower(concat('%', :title, '%'))")
    List<JobEntity> findByTitleContainingIgnoreCase(@Param("title") String title);

    @Query("select j from JobEntity j join fetch j.company "
            + "where lower(j.title) like lower(concat('%', :title, '%')) and j.status <> :status")
    List<JobEntity> findByTitleContainingIgnoreCaseAndStatusNot(@Param("title") String title,
                                                                @Param("status") JobStatus status);

    @Query("select j from JobEntity j join fetch j.company "
            + "where lower(j.title) like lower(concat('%', :title, '%')) and j.createdBy.id = :createdById")
    List<JobEntity> findByTitleContainingIgnoreCaseAndCreatedBy_Id(@Param("title") String title,
                                                                   @Param("createdById") Long createdById);
}
