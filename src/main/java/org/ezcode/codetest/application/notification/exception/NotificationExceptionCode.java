package org.ezcode.codetest.application.notification.exception;

import org.ezcode.codetest.common.base.exception.ResponseCode;
import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationExceptionCode implements ResponseCode {

	NOTIFICATION_CANNOT_FIND_EVENT_TYPE(false, HttpStatus.INTERNAL_SERVER_ERROR, "해당 이벤트 타입의 mapper를 찾을 수 없습니다."),
	NOTIFICATION_CONVERT_MESSAGE_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR, "메시지 변환 과정에서 에러가 발생했습니다."),
	NOTIFICATION_NOT_FOUND(false, HttpStatus.NOT_FOUND, "해당 ID의 notification 데이터를 찾지 못했습니다"),
	NOTIFICATION_DB_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR, "알림 데이터 저장 중 문제 발생. 서킷 브레이커가 열렸습니다.")
	;

	private final boolean success;
	private final HttpStatus status;
	private final String message;
}
