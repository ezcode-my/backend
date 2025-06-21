package org.ezcode.codetest.domain.report.model;

import java.util.Arrays;

import lombok.Getter;

@Getter
public enum ReportType {
	PROBLEM_ERROR("문제 오류"),
	PROFANITY("욕설/비속어"),
	SPAM("스팸/도배"),
	SEXUAL_CONTENT("음란성 표현"),
	HATE_SPEECH("혐오 발언"),
	PRIVACY_VIOLATION("개인정보 노출"),
	FRAUD("사기/금전 요구"),
	IMPERSONATION("도용/사칭"),
	HARASSMENT("괴롭힘"),
	POLITICAL_RELIGIOUS("정치/종교 선동"),
	OTHER("기타");

	private final String description;

	ReportType(String description) {
		this.description = description;
	}

	public static ReportType from(String value) {
		return Arrays.stream(values())
				.filter(r -> r.name().equalsIgnoreCase(value))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("목록에 없는 신고 타입입니다. : " + value));
	}
}