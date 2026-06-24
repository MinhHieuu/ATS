package com.example.ats.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
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

    public CompanyEntity(Long id, String name, String logo, String email, String website,
                         String description, String address, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.logo = logo;
        this.email = email;
        this.website = website;
        this.description = description;
        this.address = address;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
