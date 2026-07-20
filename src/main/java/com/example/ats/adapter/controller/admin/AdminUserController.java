package com.example.ats.adapter.controller.admin;

import com.example.ats.application.dto.response.UserResponse;
import com.example.ats.application.port.in.UserUseCase;
import com.example.ats.domain.model.ApiResponse;
import com.example.ats.domain.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/admin/users")
@RestController("adminUserController")
public class AdminUserController {
    private final UserUseCase userUseCase;

    public AdminUserController(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> findAll(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse<>("success", userUseCase.findAll(pageable)));
    }

    @GetMapping("search")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> searchByFullnameOrEmail(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Role role,
            @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(new ApiResponse<>("success",
                userUseCase.searchByFullnameOrEmailAndRole(keyword, role, pageable)));
    }

    @PatchMapping("{id}/active")
    public ResponseEntity<ApiResponse<UserResponse>> active(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>("success", userUseCase.activate(id)));
    }

    @PatchMapping("{id}/deactivate")
    public ResponseEntity<ApiResponse<UserResponse>> deactivate(Authentication authentication,
                                                                @PathVariable Long id) {
        Number userId = ((Jwt) authentication.getPrincipal()).getClaim("userId");
        return ResponseEntity.ok(new ApiResponse<>("success", userUseCase.deactivate(id, userId.longValue())));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>("success", userUseCase.findById(id)));
    }
}
