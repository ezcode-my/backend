package org.ezcode.codetest.domain.draft.exception;

import org.ezcode.codetest.common.base.exception.BaseException;
import org.ezcode.codetest.common.base.exception.ResponseCode;
import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class DraftException extends BaseException {

    private final ResponseCode responseCode;
    private final HttpStatus httpStatus;
    private final String message;

    public DraftException(ResponseCode responseCode) {
        this.responseCode = responseCode;
        this.httpStatus = responseCode.getStatus();
        this.message = responseCode.getMessage();
    }
}
