package com.example.ats.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UserEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;
    @Column(nullable = false, unique = true, length = 150)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(length = 20)
    private String phone;
    @Column(name = "avatar_url")
    private String avatarUrl;
    @Column(name = "is_active", nullable = false)
    private Boolean active = true;
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
    @ManyToOne()
    @JoinColumn(name = "role_id")
    private RoleEntity role;
    @OneToMany(mappedBy = "user")
    private List<ActivityLogEntity> actions;
    @OneToMany(mappedBy = "interviewer")
    private List<InterviewEntity> interviews;
    @OneToMany(mappedBy = "reviewer")
    private List<InterviewFeedbackEntity> feedbacks;
}
