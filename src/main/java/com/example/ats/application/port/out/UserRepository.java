package com.example.ats.application.port.out;

import com.example.ats.domain.model.Role;
import com.example.ats.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepository {
    User save(User user);
    User findById(Long id);
    Page<User> findAll(Pageable pageable);
    Page<User> searchByFullnameOrEmailAndRole(String keyword, Role role, Pageable pageable);
    User findByEmail(String email);
    User findByPhone(String phone);
    Boolean existsByEmail(String email);
    Boolean existsByPhone(String phone);
}
