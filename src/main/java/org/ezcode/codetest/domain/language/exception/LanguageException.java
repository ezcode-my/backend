package org.ezcode.codetest.domain.language.exception;

import org.ezcode.codetest.common.base.exception.BaseException;
import org.ezcode.codetest.common.base.exception.ResponseCode;
import org.ezcode.codetest.domain.language.exception.code.LanguageExceptionCode;
import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class LanguageException extends BaseException {

    private final ResponseCode responseCode;
    private final HttpStatus httpStatus;
    private final String message;

    public LanguageException(LanguageExceptionCode responseCode) {
        this.responseCode = responseCode;
        this.httpStatus = responseCode.getStatus();
        this.message = responseCode.getMessage();
    }
}
