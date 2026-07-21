package com.example.ats.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyResponse {
    private Long id;
    private String name;
    private String logo;
    private String email;
    private String website;
    private String description;
    private String address;
    private Instant createdAt;
    private Instant updatedAt;
    private Boolean isActive;
}
