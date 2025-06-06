package org.ezcode.codetest.domain.submission.exception.code;

import org.ezcode.codetest.common.base.exception.ResponseCode;
import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LanguageExceptionCode implements ResponseCode {

	LANGUAGE_ALREADY_EXISTS(false, HttpStatus.CONFLICT, "이미 존재하는 언어입니다."),
	LANGUAGE_NOT_FOUND(false, HttpStatus.NOT_FOUND, "존재하지 않는 언어입니다.");

	private final boolean success;
	private final HttpStatus status;
	private final String message;
}
