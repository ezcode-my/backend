package org.ezcode.codetest.infrastructure.s3.exception.code;

import org.ezcode.codetest.common.base.exception.ResponseCode;
import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum S3ExceptionCode implements ResponseCode {

	S3_UPLOAD_FAILED(false, HttpStatus.NOT_FOUND, "S3 이미지 업로드 중 오류가 발생했습니다.");

	private final boolean success;

	private final HttpStatus status;

	private final String message;
}
