package org.ezcode.codetest.application.notification.exception;

import org.ezcode.codetest.common.base.exception.ResponseCode;
import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationExceptionCode implements ResponseCode {

	NOTIFICATION_CANNOT_FIND_EVENT_TYPE(false, HttpStatus.INTERNAL_SERVER_ERROR, "해당 이벤트 타입의 mapper를 찾을 수 없습니다."),
	;

	private final boolean success;
	private final HttpStatus status;
	private final String message;
}
