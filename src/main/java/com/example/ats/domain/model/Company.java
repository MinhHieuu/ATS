package com.example.ats.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class Company {
    private Long id;
    private String name;
    private String logo;
    private String email;
    private String website;
    private String description;
    private String address;
    private Instant createdAt;
    private Instant updatedAt;
}
