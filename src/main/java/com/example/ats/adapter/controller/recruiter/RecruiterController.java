package com.example.ats.adapter.controller.recruiter;

import com.example.ats.application.dto.request.RecruiterRequest;
import com.example.ats.application.dto.response.RecruiterResponse;
import com.example.ats.application.port.in.RecruiterUseCase;
import com.example.ats.domain.model.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
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
    public ResponseEntity<ApiResponse<RecruiterResponse>> findProfile(Authentication authentication) {
        // recruiter.id dùng chung khóa chính với user.id (xem RecruiterEntity#user @MapsId)
        Number userId = ((Jwt) authentication.getPrincipal()).getClaim("userId");
        return ResponseEntity.ok(new ApiResponse<>("success", recruiterUseCase.findById(userId.longValue())));
    }

    @PatchMapping("{id}")
    public ResponseEntity<ApiResponse<RecruiterResponse>> update(@PathVariable Long id,
                                                                 @Valid @RequestBody RecruiterRequest request) {
        return ResponseEntity.ok(new ApiResponse<>("success", recruiterUseCase.update(request, id)));
    }
}
