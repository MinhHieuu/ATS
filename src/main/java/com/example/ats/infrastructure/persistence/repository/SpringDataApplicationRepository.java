package com.example.ats.infrastructure.persistence.repository;

import com.example.ats.infrastructure.persistence.entity.ApplicationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SpringDataApplicationRepository extends JpaRepository<ApplicationEntity, Long> {
    // JobApplicationMapper doc getId() cua candidate/job/resume nen moi proxy deu phai khoi tao.
    // Dung EntityGraph de fetch san candidate/job/resume ma van tuong thich voi Pageable
    // (join fetch trong @Query se sinh loi khi ap Pageable/firstResult).
    // Cac query loc theo job.createdBy chinh la co che phan quyen recruiter:
    // job mo coi (createdBy null) khong khop voi ai nen tra ve 404.

    @Override
    @EntityGraph(attributePaths = {"candidate", "candidate.user", "job", "job.company", "job.createdBy", "resume"})
    Page<ApplicationEntity> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"candidate", "candidate.user", "job", "job.company", "job.createdBy", "resume"})
    Optional<ApplicationEntity> findById(Long id);

    boolean existsByCandidate_IdAndJob_Id(Long candidateId, Long jobId);

    @EntityGraph(attributePaths = {"candidate", "candidate.user", "job", "job.company", "job.createdBy", "resume"})
    Page<ApplicationEntity> findByCandidate_Id(Long candidateId, Pageable pageable);

    @EntityGraph(attributePaths = {"candidate", "candidate.user", "job", "job.company", "job.createdBy", "resume"})
    Page<ApplicationEntity> findByJob_Id(Long jobId, Pageable pageable);

    @EntityGraph(attributePaths = {"candidate", "candidate.user", "job", "job.company", "job.createdBy", "resume"})
    @Query(value = "select a from ApplicationEntity a where a.job.createdBy.id = :createdById",
            countQuery = "select count(a) from ApplicationEntity a where a.job.createdBy.id = :createdById")
    Page<ApplicationEntity> findByJob_CreatedBy_Id(@Param("createdById") Long createdById, Pageable pageable);

    @EntityGraph(attributePaths = {"candidate", "candidate.user", "job", "job.company", "job.createdBy", "resume"})
    @Query(value = "select a from ApplicationEntity a "
            + "where a.job.id = :jobId and a.job.createdBy.id = :createdById",
            countQuery = "select count(a) from ApplicationEntity a "
            + "where a.job.id = :jobId and a.job.createdBy.id = :createdById")
    Page<ApplicationEntity> findByJob_IdAndJob_CreatedBy_Id(@Param("jobId") Long jobId,
                                                            @Param("createdById") Long createdById,
                                                            Pageable pageable);

    @EntityGraph(attributePaths = {"candidate", "candidate.user", "job", "job.company", "job.createdBy", "resume"})
    @Query("select a from ApplicationEntity a where a.id = :id and a.job.createdBy.id = :createdById")
    Optional<ApplicationEntity> findByIdAndJob_CreatedBy_Id(@Param("id") Long id,
                                                            @Param("createdById") Long createdById);

    @EntityGraph(attributePaths = {"candidate", "candidate.user", "job", "job.company", "job.createdBy", "resume"})
    @Query("select a from ApplicationEntity a where a.id = :id and a.candidate.id = :candidateId")
    Optional<ApplicationEntity> findByIdAndCandidate_Id(@Param("id") Long id,
                                                        @Param("candidateId") Long candidateId);
}
