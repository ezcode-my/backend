package org.ezcode.codetest.domain.community.exception;

import org.ezcode.codetest.common.base.exception.ResponseCode;
import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommunityExceptionCode implements ResponseCode {

	DISCUSSION_NOT_FOUND(false, HttpStatus.NOT_FOUND, "해당 ID의 토론글이 존재하지 않습니다."),
	DISCUSSION_PROBLEM_MISMATCH(false, HttpStatus.BAD_REQUEST, "해당 글이 요청된 문제에 속하지 않습니다."),
	
	REPLY_NOT_FOUND(false, HttpStatus.NOT_FOUND, "해당 ID의 댓글이 존재하지 않습니다."),
	REPLY_DISCUSSION_MISMATCH(false, HttpStatus.BAD_REQUEST, "해당 댓글이 요청된 토론글에 속하지 않습니다."),
	REPLY_PARENT_ALREADY_EXISTS(false, HttpStatus.BAD_REQUEST, "이미 부모 댓글이 있는 댓글에는 대댓글을 작성할 수 없습니다."),

	USER_NOT_AUTHOR(false, HttpStatus.FORBIDDEN, "작성자만 수정/삭제할 수 있습니다."),
	;

	private final boolean success;
	private final HttpStatus status;
	private final String message;
}
