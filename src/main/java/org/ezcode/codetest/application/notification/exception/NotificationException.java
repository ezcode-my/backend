package org.ezcode.codetest.application.notification.exception;

import org.ezcode.codetest.common.base.exception.BaseException;
import org.ezcode.codetest.common.base.exception.ResponseCode;
import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class NotificationException extends BaseException {

	private final ResponseCode responseCode;
	private final HttpStatus httpStatus;
	private final String message;

	public NotificationException(ResponseCode responseCode) {
		this.responseCode = responseCode;
		this.httpStatus = responseCode.getStatus();
		this.message = responseCode.getMessage();
	}

	public NotificationException(ResponseCode responseCode, String message) {
		this.responseCode = responseCode;
		this.httpStatus = responseCode.getStatus();
		this.message = responseCode.getMessage() + " : " + message;
	}

	public NotificationException(ResponseCode responseCode, Throwable cause, String message) {
		super();

		this.responseCode = responseCode;
		this.httpStatus = responseCode.getStatus();
		super.initCause(cause);
		this.message = message;
	}
}
