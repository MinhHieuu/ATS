package com.example.ats.adapter.controller.admin;

import com.example.ats.application.dto.response.AuditLogResponse;
import com.example.ats.application.port.in.AuditLogUseCase;
import com.example.ats.domain.model.ApiResponse;
import com.example.ats.domain.model.AuditAction;
import com.example.ats.domain.model.AuditEntityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController("adminAuditLogController")
@RequestMapping("/api/admin/audit-logs")
public class AdminAuditLogController {
    private final AuditLogUseCase auditLogUseCase;

    public AdminAuditLogController(AuditLogUseCase auditLogUseCase) {
        this.auditLogUseCase = auditLogUseCase;
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<Page<AuditLogResponse>>> search(
            @RequestParam(required = false) Long actorId,
            @RequestParam(required = false) AuditEntityType entityType,
            @RequestParam(required = false) AuditAction action,
            @RequestParam(required = false) Long from,
            @RequestParam(required = false) Long to,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Instant fromInstant = from != null ? Instant.ofEpochSecond(from) : null;
        Instant toInstant = to != null ? Instant.ofEpochSecond(to) : null;
        return ResponseEntity.ok(new ApiResponse<>("success",
                auditLogUseCase.search(actorId, entityType, action, fromInstant, toInstant, pageable)));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<AuditLogResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>("success", auditLogUseCase.findById(id)));
    }

    @GetMapping("entity/{entityType}/{entityId}")
    public ResponseEntity<ApiResponse<Page<AuditLogResponse>>> findByEntity(
            @PathVariable AuditEntityType entityType,
            @PathVariable Long entityId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse<>("success",
                auditLogUseCase.findByEntity(entityType, entityId, pageable)));
    }
}
