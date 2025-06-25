package org.ezcode.codetest.infrastructure.s3.exception;

import org.ezcode.codetest.common.base.exception.BaseException;
import org.ezcode.codetest.common.base.exception.ResponseCode;
import org.ezcode.codetest.infrastructure.s3.exception.code.S3ExceptionCode;
import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class S3Exception extends BaseException {

  private final ResponseCode responseCode;

  private final HttpStatus httpStatus;

  private final String message;

  public S3Exception(S3ExceptionCode responseCode) {
      this.responseCode = responseCode;
      this.httpStatus = responseCode.getStatus();
      this.message = responseCode.getMessage();
  }
}
