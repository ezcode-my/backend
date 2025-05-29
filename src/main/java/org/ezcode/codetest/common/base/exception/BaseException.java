package org.ezcode.codetest.common.base.exception;

import org.springframework.http.HttpStatus;

public abstract class BaseException extends RuntimeException {

	public abstract ResponseCode getResponseCode();

	public abstract HttpStatus getHttpStatus();

}
