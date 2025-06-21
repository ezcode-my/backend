package org.ezcode.codetest.application.report.service;

import org.ezcode.codetest.application.report.dto.*;
import org.ezcode.codetest.domain.report.model.*;
import org.ezcode.codetest.domain.report.repository.ReportRepository;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.repository.UserRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final UserRepository userRepository;
    private final ReportRepository reportRepository;

    //신규 신고 생성
    @Transactional
    public ReportResponse submitReport(Long reporterId, ReportRequest request) {
        User reporter = userRepository.findUserById(reporterId)
                .orElseThrow(() -> new IllegalArgumentException("신고자 없음"));

        ReportTargetType targetType = ReportTargetType.valueOf(request.targetType().toUpperCase());
        ReportType reportType      = ReportType.from(request.reportType());

        if (targetType == ReportTargetType.USER && reporterId.equals(request.targetId())) {
            throw new IllegalArgumentException("자기 자신은 신고할 수 없습니다.");
        }

        Report report = new Report(reporter,
                request.targetId(),
                targetType,
                request.message(),
                request.imageUrl(),
                reportType);

        reportRepository.save(report);

        return new ReportResponse(
                report.getId(),
                report.getReportStatus().name(),
                report.getReportType().name(),
                report.getMessage(),
                report.getImageUrl()
        );
    }

    //신고 목록 페이징
    @Transactional(readOnly = true)
    public Page<ReportListResponse> getReportList(Pageable pageable) {
        return reportRepository.findAll(pageable)
                .map(r -> new ReportListResponse(
                        r.getId(),
                        r.getReporter().getEmail(),
                        r.getTargetId(),
                        r.getTargetType().name(),
                        r.getReportType().name(),
                        r.getReportStatus().name(),
                        r.getMessage(),
                        r.getCreatedAt().toString()
                ));
    }

    //신고 상세
    @Transactional(readOnly = true)
    public ReportDetailResponse getReportDetail(Long reportId) {
        Report r = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("신고가 존재하지 않습니다."));

        return new ReportDetailResponse(
                r.getId(),
                r.getReporter().getEmail(),
                r.getTargetId(),
                r.getTargetType().name(),
                r.getReportType().name(),
                r.getReportStatus().name(),
                r.getMessage(),
                r.getImageUrl(),
                r.getCreatedAt().toString(),
                r.getModifiedAt().toString()
        );
    }

    //신고 상태 변경
    @Transactional
    public void updateReportStatus(Long reportId, ReportStatusUpdateRequest req) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("신고가 존재하지 않습니다."));

        ReportStatus newStatus = req.toEnum();
        report.updateStatus(newStatus);
    }
}
