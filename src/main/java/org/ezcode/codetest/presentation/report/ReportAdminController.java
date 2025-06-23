package org.ezcode.codetest.presentation.report;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ezcode.codetest.application.report.dto.*;
import org.ezcode.codetest.application.report.service.ReportService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports/admin")
public class ReportAdminController {

    private final ReportService reportService;

    // 신고 목록 조회
    @GetMapping
    public ResponseEntity<Page<ReportListResponse>> list(
            @ParameterObject
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {

        return ResponseEntity.ok(reportService.getReportList(pageable));
    }

    // 신고 조회
    @GetMapping("/{reportId}")
    public ResponseEntity<ReportDetailResponse> detail(@PathVariable Long reportId) {
        return ResponseEntity.ok(reportService.getReportDetail(reportId));
    }

    // 신고 상태 변경
    @PatchMapping("/{reportId}/status")
    public ResponseEntity<ReportChangeResponse> changeStatus(
            @PathVariable Long reportId,
            @RequestBody @Valid ReportStatusUpdateRequest request) {

        reportService.updateReportStatus(reportId, request);
        String message = request.newStatus() + "로 변경 완료되었습니다.";
        return ResponseEntity.ok(new ReportChangeResponse(message));
    }


}
