package org.ezcode.codetest.domain.report.model;

import java.util.Arrays;

import lombok.Getter;

@Getter
public enum ReportStatus {
	PENDING("대기 중"),        // 대기 중
	IN_PROGRESS("검토 중"),    // 검토 중
	RESOLVED("조치 완료"),       // 조치 완료
	REJECTED("기각됨"),       // 기각됨
	CANCELED("사용자 철회");        // 사용자 철회 (옵션);

	private final String description;

	ReportStatus(String description) {
		this.description = description;
	}

	public static ReportStatus from(String reportStatus) {
		return Arrays.stream(ReportStatus.values())
			.filter(r -> r.name().equalsIgnoreCase(reportStatus))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("Invalid reportStatus input : " + reportStatus));
	}
}
