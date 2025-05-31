package org.ezcode.codetest.domain.chat.exception;

import org.ezcode.codetest.common.base.exception.ResponseCode;
import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChattingExceptionCode implements ResponseCode {

	CHATTING_NOT_FOUND(false, HttpStatus.NOT_FOUND, "해당채팅이 조회되지 않습니다."),
	CHATTING_ROOM_NOT_FOUND(false, HttpStatus.NOT_FOUND, "해당 채팅방이 조회되지 않습니다."),
	INVALID_CHATTING_SESSION(false, HttpStatus.NOT_FOUND, "해당 채팅 세션이 조회되지 않습니다.");

	private final boolean success;
	private final HttpStatus status;
	private final String message;
}
