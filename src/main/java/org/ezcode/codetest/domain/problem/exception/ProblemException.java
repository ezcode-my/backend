package org.ezcode.codetest.domain.problem.exception;

import org.ezcode.codetest.common.base.exception.BaseException;
import org.ezcode.codetest.common.base.exception.ResponseCode;
import org.ezcode.codetest.domain.problem.exception.code.ProblemExceptionCode;
import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ProblemException extends BaseException {

    private final ResponseCode responseCode;

    private final HttpStatus httpStatus;

    private final String message;

	public ProblemException(ProblemExceptionCode responseCode) {
		this.responseCode = responseCode;
		this.httpStatus = responseCode.getStatus();
		this.message = responseCode.getMessage();
	}
}
