package org.ezcode.codetest.application.report.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReportRequest(
        @NotNull
        Long targetId,

        @NotBlank
        String targetType,

        @NotBlank
        String reportType,

        @NotBlank
        String message,

        String imageUrl
) {}
