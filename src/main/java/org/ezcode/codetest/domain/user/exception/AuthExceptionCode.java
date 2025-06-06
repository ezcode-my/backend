package org.ezcode.codetest.domain.user.exception;

import org.ezcode.codetest.common.base.exception.ResponseCode;
import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthExceptionCode implements ResponseCode {
	NO_AUTH_INFO(false, HttpStatus.BAD_REQUEST, "인증 정보가 없습니다."),
	EXIST_USER_EMAIL(false, HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),
	USER_NOT_FOUND(false, HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다"),
	PASSWORD_NOT_MATCH(false, HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다"),
	LOGOUT_USER(false, HttpStatus.BAD_REQUEST, "이미 로그아웃한 유저입니다."),
	INVALID_AUTHORIZATION_HEADER(false, HttpStatus.BAD_REQUEST, "유효하지 않은 Authorization 헤더"),
	AUTH_TYPE_MISMATCH(false, HttpStatus.BAD_REQUEST, "소셜 가입 회원입니다"),
	INVALID_REFRESH_TOKEN(false, HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않거나 없습니다")

	;

	private final boolean success;
	private final HttpStatus status;
	private final String message;
}
