package org.ezcode.codetest.domain.chat.exception;

import org.ezcode.codetest.common.base.exception.BaseException;
import org.ezcode.codetest.common.base.exception.ResponseCode;
import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ChattingException extends BaseException {

	private final ResponseCode responseCode;
	private final HttpStatus httpStatus;
	private final String message;

	public ChattingException(ResponseCode responseCode) {
		this.responseCode = responseCode;
		this.httpStatus = responseCode.getStatus();
		this.message = responseCode.getMessage();
	}
}
