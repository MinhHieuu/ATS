package com.example.ats.application.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecruiterRequest extends UserRequest {
    private Long companyId;

    @Size(max = 255, message = "Position must not exceed 255 characters")
    private String position;
}
