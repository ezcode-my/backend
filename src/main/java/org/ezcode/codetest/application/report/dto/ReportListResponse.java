package org.ezcode.codetest.application.report.dto;

public record ReportListResponse(
        Long reportId,
        String reporterEmail,
        Long targetId,
        String targetType,
        String reportType,
        String reportStatus,
        String message,
        String createdAt
) {}
