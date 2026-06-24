package com.example.ats.infrastructure.persistence.adapter;

import com.example.ats.application.port.out.UserRepository;
import com.example.ats.domain.exception.ResourceNotFoundException;
import com.example.ats.domain.model.User;
import com.example.ats.infrastructure.persistence.entity.UserEntity;
import com.example.ats.infrastructure.persistence.repository.SpringDataUserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Repository;

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

    private User toUser(UserEntity entity) {
        User user = new User(entity.getId(), entity.getEmail(), entity.getFullname(), entity.getPassword(),
                entity.getPhone(), entity.getAvatarUrl(), entity.getActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(), entity.getRole());
        return user;
    }
}
