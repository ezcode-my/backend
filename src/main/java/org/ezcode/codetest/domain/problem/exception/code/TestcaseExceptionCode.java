package org.ezcode.codetest.domain.problem.exception.code;

import org.ezcode.codetest.common.base.exception.ResponseCode;
import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TestcaseExceptionCode implements ResponseCode {

	TESTCASE_NOT_FOUND(false, HttpStatus.NOT_FOUND, "존재하지 않는 테스트 케이스 입니다.");

	private final boolean success;
	private final HttpStatus status;
	private final String message;

}
