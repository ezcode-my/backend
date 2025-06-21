package org.ezcode.codetest.presentation.report;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ezcode.codetest.application.report.dto.ReportRequest;
import org.ezcode.codetest.application.report.dto.ReportResponse;
import org.ezcode.codetest.application.report.service.ReportService;
import org.ezcode.codetest.domain.user.model.entity.AuthUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<ReportResponse> report(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody @Valid ReportRequest request
    ) {
        return ResponseEntity.ok(reportService.submitReport(authUser.getId(), request));
    }

}
