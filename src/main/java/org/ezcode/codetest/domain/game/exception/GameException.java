package org.ezcode.codetest.domain.game.exception;

import org.ezcode.codetest.common.base.exception.BaseException;
import org.ezcode.codetest.common.base.exception.ResponseCode;
import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class GameException extends BaseException {

	private final ResponseCode responseCode;
	private final HttpStatus httpStatus;
	private final String message;

	public GameException(ResponseCode responseCode) {
		this.responseCode = responseCode;
		this.httpStatus = responseCode.getStatus();
		this.message = responseCode.getMessage();
	}
}
