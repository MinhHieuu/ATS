package com.example.ats.infrastructure.persistence.repository;

import com.example.ats.domain.model.Role;
import com.example.ats.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SpringDataUserRepository extends JpaRepository<UserEntity, Long> {
    // UserEntity.candidate va UserEntity.recruiter la @OneToOne(mappedBy) nen mac dinh EAGER,
    // va phia nghich cua @OneToOne khong lazy-proxy duoc (Hibernate phai query moi biet null hay khong).
    // Dung EntityGraph de tach truy van count/data cho Pageable ma van fetch san 2 quan he,
    // tranh 2N+1 khi map sang UserResponse.

    @Override
    @EntityGraph(attributePaths = {"candidate", "recruiter"})
    Page<UserEntity> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"candidate", "recruiter"})
    @Query(value = "select u from UserEntity u "
            + "where (lower(u.fullname) like lower(concat(:keyword, '%')) "
            + "or lower(u.email) like lower(concat(:keyword, '%'))) "
            + "and u.role = :role",
            countQuery = "select count(u) from UserEntity u "
            + "where (lower(u.fullname) like lower(concat(:keyword, '%')) "
            + "or lower(u.email) like lower(concat(:keyword, '%'))) "
            + "and u.role = :role")
    Page<UserEntity> searchByFullnameOrEmailAndRole(@Param("keyword") String keyword,
                                                   @Param("role") Role role,
                                                   Pageable pageable);

    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByPhone(String phone);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}
