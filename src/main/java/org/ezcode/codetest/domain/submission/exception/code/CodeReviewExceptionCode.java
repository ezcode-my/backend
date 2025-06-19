package org.ezcode.codetest.domain.submission.exception.code;

import org.ezcode.codetest.common.base.exception.ResponseCode;
import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CodeReviewExceptionCode implements ResponseCode {

	REVIEW_SERVER_ERROR(false, HttpStatus.BAD_GATEWAY, "AI 서버 오류가 발생했습니다."),
	REVIEW_TIMEOUT(false, HttpStatus.GATEWAY_TIMEOUT, "AI 응답 시간이 초과되었습니다."),
	REVIEW_INVALID_FORMAT(false, HttpStatus.INTERNAL_SERVER_ERROR, "AI 리뷰 형식 검증에 실패했습니다."),
	ALREADY_REVIEWING(false, HttpStatus.CONFLICT, "이미 해당 코드에 대한 리뷰가 진행 중입니다."),
	;

	private final boolean success;
	private final HttpStatus status;
	private final String message;
}
