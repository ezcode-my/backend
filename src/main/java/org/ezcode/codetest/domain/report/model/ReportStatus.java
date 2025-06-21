package org.ezcode.codetest.domain.report.model;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum ReportStatus {
	PENDING("대기 중"),
	IN_PROGRESS("검토 중"),
	RESOLVED("조치 완료"),
	REJECTED("기각됨"),
	CANCELED("사용자 철회");

	private final String description;

	ReportStatus(String description) {
		this.description = description;
	}

	@JsonCreator
	public static ReportStatus from(String reportStatus) {
		return Arrays.stream(ReportStatus.values())
			.filter(r -> r.name().equalsIgnoreCase(reportStatus))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("목록에 없는 신고 상태 입니다. : " + reportStatus));
	}
}
