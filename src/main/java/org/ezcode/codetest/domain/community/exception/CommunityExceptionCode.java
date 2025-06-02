package org.ezcode.codetest.domain.community.exception;

import org.ezcode.codetest.common.base.exception.ResponseCode;
import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommunityExceptionCode implements ResponseCode {

	DISCUSSION_NOT_FOUND(false, HttpStatus.NOT_FOUND, "해당 ID의 글이 존재하지 않습니다."),
	DISCUSSION_PROBLEM_MISMATCH(false, HttpStatus.BAD_REQUEST, "해당 글이 요청된 문제에 속하지 않습니다."),
	USER_NOT_AUTHOR(false, HttpStatus.FORBIDDEN, "작성자만 수정/삭제할 수 있습니다.");

	private final boolean success;
	private final HttpStatus status;
	private final String message;
}
