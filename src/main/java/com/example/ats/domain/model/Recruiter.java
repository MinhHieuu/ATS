package com.example.ats.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Recruiter {
    private Long id;
    private Long userId;
    private Long companyId;
    private String position;
}
