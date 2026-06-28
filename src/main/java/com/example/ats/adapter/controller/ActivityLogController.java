package com.example.ats.adapter.controller;

import com.example.ats.application.dto.request.ActivityLogRequest;
import com.example.ats.application.dto.response.ActivityLogResponse;
import com.example.ats.application.port.in.ActivityLogUseCase;
import com.example.ats.domain.model.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activity-logs")
public class ActivityLogController {
    private final ActivityLogUseCase useCase;

    public ActivityLogController(ActivityLogUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ActivityLogResponse>> create(@Valid @RequestBody ActivityLogRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("create success", useCase.create(request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ActivityLogResponse>>> findAll() {
        return ResponseEntity.ok(new ApiResponse<>("success", useCase.findAll()));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<ActivityLogResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>("success", useCase.findById(id)));
    }

    @GetMapping("user/{userId}")
    public ResponseEntity<ApiResponse<List<ActivityLogResponse>>> findByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(new ApiResponse<>("success", useCase.findByUser(userId)));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        useCase.delete(id);
        return ResponseEntity.ok(new ApiResponse<>("delete success", null));
    }
}
