package org.ezcode.codetest.application.report.dto;

import jakarta.validation.constraints.NotBlank;
import org.ezcode.codetest.domain.report.model.ReportStatus;

public record ReportStatusUpdateRequest(
        @NotBlank
        String newStatus,   // 예시 RESOLVED(해결됨), REJECTED(거절)
        String resultMessage
) {
        public ReportStatus toEnum() {
                return ReportStatus.from(newStatus);
        }
}
