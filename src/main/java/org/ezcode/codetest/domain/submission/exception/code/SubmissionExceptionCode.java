package org.ezcode.codetest.domain.submission.exception.code;

import org.ezcode.codetest.common.base.exception.ResponseCode;
import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SubmissionExceptionCode implements ResponseCode {

	EMITTER_NOT_FOUND(false, HttpStatus.NOT_FOUND, "emitter를 찾을 수 없습니다."),
	EMITTER_SEND_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR, "SSE 전송 중 오류가 발생했습니다."),
	COMPILE_SERVER_ERROR(false, HttpStatus.BAD_GATEWAY, "컴파일 서버 오류"),
	COMPILE_TIMEOUT(false, HttpStatus.GATEWAY_TIMEOUT, "컴파일 서버로부터 응답이 지연되고 있습니다."),
	TESTCASE_TIMEOUT(false, HttpStatus.GATEWAY_TIMEOUT, "테스트케이스 채점 시간이 초과되었습니다."),
	;
	private final boolean success;
	private final HttpStatus status;
	private final String message;
}
