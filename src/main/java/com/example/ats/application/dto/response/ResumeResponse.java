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
public class ResumeResponse {
    private Long id;
    private Long candidateId;
    private String fileName;
    private String fileUrl;
    private Instant createdAt;
}
