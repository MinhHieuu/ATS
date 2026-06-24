package com.example.ats.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
public class CompanyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column()
    private String logo;
    @Column(nullable = false)
    private String email;
    @Column
    private String website;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private String address;
    @Column(name = "create_at")
    private Instant createdAt;
    @Column(name = "update_at")
    private Instant updatedAt;
    @OneToMany(mappedBy = "company")
    private List<RecruiterEntity> recruiters;

}
