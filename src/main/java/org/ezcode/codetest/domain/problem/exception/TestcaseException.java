package org.ezcode.codetest.domain.problem.exception;

import org.ezcode.codetest.common.base.exception.BaseException;
import org.ezcode.codetest.common.base.exception.ResponseCode;
import org.ezcode.codetest.domain.problem.exception.code.TestcaseExceptionCode;
import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class TestcaseException extends BaseException {

  private final ResponseCode responseCode;

  private final HttpStatus httpStatus;

  private final String message;

	public TestcaseException(TestcaseExceptionCode responseCode) {
		this.responseCode = responseCode;
		this.httpStatus = responseCode.getStatus();
		this.message = responseCode.getMessage();
	}
}
