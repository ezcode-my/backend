package org.ezcode.codetest.domain.submission.exception;

import org.ezcode.codetest.common.base.exception.BaseException;
import org.ezcode.codetest.common.base.exception.ResponseCode;
import org.ezcode.codetest.domain.submission.exception.code.GitHubExceptionCode;
import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class GitHubClientException extends BaseException {

    private final ResponseCode responseCode;
    private final HttpStatus httpStatus;
    private final String message;

    public GitHubClientException(GitHubExceptionCode responseCode) {
        this.responseCode = responseCode;
        this.httpStatus = responseCode.getStatus();
        this.message = responseCode.getMessage();
    }
}
