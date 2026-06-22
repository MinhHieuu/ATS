package com.example.ats.domain.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ApiResponse<T> {
    private String message;
    private T data;
}
