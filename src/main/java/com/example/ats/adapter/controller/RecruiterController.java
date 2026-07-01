package com.example.ats.adapter.controller;

import com.example.ats.application.dto.request.RecruiterRequest;
import com.example.ats.application.dto.response.RecruiterResponse;
import com.example.ats.application.port.in.RecruiterUseCase;
import com.example.ats.domain.model.ApiResponse;
import com.example.ats.domain.model.User;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recruiters")
public class RecruiterController {
    private final RecruiterUseCase recruiterUseCase;

    public RecruiterController(RecruiterUseCase recruiterUseCase) {
        this.recruiterUseCase = recruiterUseCase;
    }


    @GetMapping("")
    public ResponseEntity<ApiResponse<List<RecruiterResponse>>> findAll() {
        return ResponseEntity.ok(new ApiResponse<>("success", recruiterUseCase.findAll()));
    }

    @GetMapping("profile")
    public ResponseEntity<ApiResponse<RecruiterResponse>> findByUserId(Authentication authentication) {
        Long userId = ((User) authentication.getPrincipal()).getId();
        return ResponseEntity.ok(new ApiResponse<>("success", recruiterUseCase.findByUserId(userId)));
    }

    @PatchMapping("{id}")
    public ResponseEntity<ApiResponse<RecruiterResponse>> update(@PathVariable Long id,
                                                                 @Valid @RequestBody RecruiterRequest request) {
        return ResponseEntity.ok(new ApiResponse<>("success", recruiterUseCase.update(request, id)));
    }
}
