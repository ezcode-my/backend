package org.ezcode.codetest.domain.problem.exception.code;

import org.ezcode.codetest.common.base.exception.ResponseCode;
import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SubmissionExceptionCode implements ResponseCode {

;
	private final boolean success;
	private final HttpStatus status;
	private final String message;
}
