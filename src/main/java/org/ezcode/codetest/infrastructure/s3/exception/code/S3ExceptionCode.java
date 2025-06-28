package org.ezcode.codetest.infrastructure.s3.exception.code;

import org.ezcode.codetest.common.base.exception.ResponseCode;
import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum S3ExceptionCode implements ResponseCode {

	S3_UPLOAD_FAILED(false, HttpStatus.INTERNAL_SERVER_ERROR, "S3 이미지 업로드 중 오류가 발생 했습니다."),
	S3_INVALID_FILE_TYPE(false, HttpStatus.BAD_REQUEST, "이미지 파일만 업로드할 수 있습니다."),
	S3_DELETE_FAILED(false, HttpStatus.INTERNAL_SERVER_ERROR, "S3 이미지 삭제 중 오류가 발생 했습니다.");
	private final boolean success;

	private final HttpStatus status;

	private final String message;
}
