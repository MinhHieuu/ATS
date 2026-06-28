package com.example.ats.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResumeRequest {
    @NotNull(message = "Candidate id is required")
    private Long candidateId;
    @NotBlank(message = "File name is required")
    private String fileName;
    @NotBlank(message = "File url is required")
    private String fileUrl;
}
