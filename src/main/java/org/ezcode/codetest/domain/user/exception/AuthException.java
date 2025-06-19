package org.ezcode.codetest.domain.user.exception;

import org.ezcode.codetest.common.base.exception.BaseException;
import org.ezcode.codetest.common.base.exception.ResponseCode;
import org.ezcode.codetest.domain.user.exception.code.AuthExceptionCode;
import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class AuthException extends BaseException {

	private final ResponseCode responseCode;
	private final HttpStatus httpStatus;
	private final String message;

	public AuthException(AuthExceptionCode responseCode) {
		this.responseCode = responseCode;
		this.httpStatus = responseCode.getStatus();
		this.message = responseCode.getMessage();
	}
}
