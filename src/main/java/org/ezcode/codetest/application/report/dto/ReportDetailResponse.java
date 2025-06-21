package org.ezcode.codetest.application.report.dto;

public record ReportDetailResponse(
        Long reportId,
        String reporterEmail,
        Long targetId,
        String targetType,
        String reportType,
        String reportStatus,
        String message,
        String imageUrl,
        String createdAt,
        String modifiedAt
) {}
