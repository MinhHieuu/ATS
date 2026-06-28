package com.example.ats.adapter.controller;

import com.example.ats.application.dto.request.ApplicationStageRequest;
import com.example.ats.application.dto.response.ApplicationStageResponse;
import com.example.ats.application.port.in.ApplicationStageUseCase;
import com.example.ats.domain.model.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/application-stages")
public class ApplicationStageController {
    private final ApplicationStageUseCase useCase;

    public ApplicationStageController(ApplicationStageUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ApplicationStageResponse>> create(@Valid @RequestBody ApplicationStageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("create success", useCase.create(request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ApplicationStageResponse>>> findAll() {
        return ResponseEntity.ok(new ApiResponse<>("success", useCase.findAll()));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<ApplicationStageResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>("success", useCase.findById(id)));
    }

    @PatchMapping("{id}")
    public ResponseEntity<ApiResponse<ApplicationStageResponse>> update(@PathVariable Long id,
                                                                        @Valid @RequestBody ApplicationStageRequest request) {
        return ResponseEntity.ok(new ApiResponse<>("success", useCase.update(id, request)));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        useCase.delete(id);
        return ResponseEntity.ok(new ApiResponse<>("delete success", null));
    }
}
