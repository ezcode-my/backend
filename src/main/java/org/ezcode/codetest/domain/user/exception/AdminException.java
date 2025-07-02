package org.ezcode.codetest.domain.user.exception;

import org.ezcode.codetest.common.base.exception.ResponseCode;
import org.ezcode.codetest.domain.user.exception.code.AdminExceptionCode;
import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class AdminException extends RuntimeException {
    private final AdminExceptionCode responseCode;
    private final HttpStatus httpStatus;
    private final String message;

    public AdminException(AdminExceptionCode responseCode) {
        this.responseCode = responseCode;
        this.httpStatus = responseCode.getStatus();
        this.message = responseCode.getMessage();
    }
}
