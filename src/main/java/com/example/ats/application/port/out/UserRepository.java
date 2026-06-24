package com.example.ats.application.port.out;

import com.example.ats.domain.model.User;

public interface UserRepository {
    User save(User user);
    User findById(Long id);
    User findByEmail(String email);
    User findByPhone(String phone);
    Boolean existsByEmail(String email);
    Boolean existsByPhone(String phone);
}
