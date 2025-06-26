package org.ezcode.codetest.application.report.dto;

import org.ezcode.codetest.domain.report.model.Report;

public record ReportMyResponse(
        Long id,
        Long targetId,
        String targetType,
        String reportType,
        String reportStatus,
        String message,
        String imageUrl,
        String resultMessage,
        String createdAt
) {
    public static ReportMyResponse from(Report r) {
        return new ReportMyResponse(
                r.getId(),
                r.getTargetId(),
                r.getTargetType().name(),
                r.getReportType().name(),
                r.getReportStatus().name(),
                r.getMessage(),
                r.getImageUrl(),
                r.getResultMessage(),
                r.getCreatedAt().toString()
        );
    }
}
