package org.ezcode.codetest.application.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ReportResponse", description = "신고 DTO")
public record ReportResponse(
        Long reportId,
        String status,
        String type,
        String message,
        String imageUrl
) {}
