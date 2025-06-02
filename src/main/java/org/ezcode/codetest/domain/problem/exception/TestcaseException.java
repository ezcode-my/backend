package org.ezcode.codetest.domain.problem.exception;

import org.ezcode.codetest.common.base.exception.BaseException;
import org.ezcode.codetest.common.base.exception.ResponseCode;
import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class TestcaseException extends BaseException {

  private final ResponseCode responseCode;

  private final HttpStatus httpStatus;

  private final String message;

	public TestcaseException(ResponseCode responseCode, HttpStatus httpStatus, String message) {
		this.responseCode = responseCode;
		this.httpStatus = httpStatus;
		this.message = message;
	}
}
