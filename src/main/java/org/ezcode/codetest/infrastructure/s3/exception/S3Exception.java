package org.ezcode.codetest.infrastructure.s3.exception;

import org.ezcode.codetest.common.base.exception.BaseException;
import org.ezcode.codetest.common.base.exception.ResponseCode;
import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class S3Exception extends BaseException {

  private final ResponseCode responseCode;

  private final HttpStatus httpStatus;

  private final String message;

  public S3Exception(ResponseCode responseCode, HttpStatus httpStatus, String message) {
      this.responseCode = responseCode;
      this.httpStatus = httpStatus;
      this.message = message;
  }
}
