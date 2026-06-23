package com.example.ats.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class Resume {
    private Long id;
    private Long candidateId;
    private String fileName;
    private String fileUrl;
    private Instant createdAt;
}
