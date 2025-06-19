package org.ezcode.codetest.domain.user.exception.code;

import org.ezcode.codetest.common.base.exception.ResponseCode;
import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserExceptionCode implements ResponseCode {

	NOT_ENOUGH_TOKEN(false, HttpStatus.BAD_REQUEST, "리뷰 토큰이 부족합니다."),
	;

	private final boolean success;
	private final HttpStatus status;
	private final String message;

}
