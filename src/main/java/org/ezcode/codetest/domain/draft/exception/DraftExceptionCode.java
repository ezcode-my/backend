package org.ezcode.codetest.domain.draft.exception;

import org.ezcode.codetest.common.base.exception.ResponseCode;
import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DraftExceptionCode implements ResponseCode {

	DRAFT_NOT_FOUND(false, HttpStatus.NOT_FOUND, "해당 임시 저장 내역이 존재하지 않습니다."),

	// 409 Conflict: 낙관적 락 충돌 발생 시 사용
	DRAFT_VERSION_CONFLICT(false, HttpStatus.CONFLICT, "다른 탭에서 저장된 최신 코드가 존재합니다. 새로고침이 필요합니다."),

	// 본인의 Draft가 아닌데 조회/수정하려 할 때 (URL 조작 등)
	DRAFT_ACCESS_DENIED(false, HttpStatus.FORBIDDEN, "해당 임시 저장 내역에 접근할 권한이 없습니다."),
	;

	private final boolean success;
	private final HttpStatus status;
	private final String message;
}
