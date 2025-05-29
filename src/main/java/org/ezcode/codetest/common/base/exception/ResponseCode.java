package org.ezcode.codetest.common.base.exception;

import org.springframework.http.HttpStatus;

public interface ResponseCode {

	HttpStatus getStatus();

	String getMessage();

	boolean isSuccess();
}