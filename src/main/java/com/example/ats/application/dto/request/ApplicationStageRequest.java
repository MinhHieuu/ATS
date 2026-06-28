package com.example.ats.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationStageRequest {
    @NotBlank(message = "Stage name is required")
    @Size(max = 100, message = "Stage name must not exceed 100 characters")
    private String name;

    @NotNull(message = "Position is required")
    @PositiveOrZero(message = "Position must be greater than or equal to 0")
    private Integer position;
}
