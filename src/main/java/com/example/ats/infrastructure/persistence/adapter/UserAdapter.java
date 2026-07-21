package com.example.ats.infrastructure.persistence.adapter;

import com.example.ats.application.port.out.UserRepository;
import com.example.ats.domain.exception.ResourceNotFoundException;
import com.example.ats.domain.model.Role;
import com.example.ats.domain.model.User;
import com.example.ats.infrastructure.persistence.entity.UserEntity;
import com.example.ats.infrastructure.persistence.repository.SpringDataUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserAdapter implements UserRepository {
    private final SpringDataUserRepository userRepository;
    public UserAdapter(SpringDataUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        return toUser(userRepository.save(UserEntity.builder()
                        .id(user.getId())
                        .fullname(user.getFullname())
                        .email(user.getEmail())
                        .password(user.getPassword())
                        .avatarUrl(user.getAvatarUrl())
                        .phone(user.getPhone())
                        .active(user.getActive())
                        .createdAt(user.getCreatedAt())
                        .updatedAt(user.getUpdatedAt())
                        .role(user.getRole())
                        .build())
        );
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .map(this::toUser)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::toUser);
    }

    @Override
    public Page<User> searchByFullnameOrEmailAndRole(String keyword, Role role, Pageable pageable) {
        return userRepository.searchByFullnameOrEmailAndRole(keyword, role, pageable).map(this::toUser);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).map(this::toUser)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));
    }

    @Override
    public User findByPhone(String phone) {
        return userRepository.findByPhone(phone).map(this::toUser)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Boolean existsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    @Override
    public List<Long> findIdsByRole(Role role) {
        return userRepository.findIdsByRole(role);
    }

    private User toUser(UserEntity entity) {
        User user = new User(entity.getId(), entity.getEmail(), entity.getFullname(), entity.getPassword(),
                entity.getPhone(), entity.getAvatarUrl(), entity.getActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(), entity.getRole());
        return user;
    }
}
