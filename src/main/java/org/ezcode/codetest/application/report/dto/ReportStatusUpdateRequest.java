package org.ezcode.codetest.application.report.dto;

import jakarta.validation.constraints.NotBlank;

public record ReportStatusUpdateRequest(
        @NotBlank
        String newStatus   // 예시 RESOLVED(해결됨), REJECTED(거절)
) {}
