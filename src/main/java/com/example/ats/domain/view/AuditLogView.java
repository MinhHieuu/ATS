package com.example.ats.domain.view;

import com.example.ats.domain.model.AuditLog;

public record AuditLogView(
        AuditLog log,
        String actorName
) {}
