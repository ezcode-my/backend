package org.ezcode.codetest.domain.user.exception.code;

import org.ezcode.codetest.common.base.exception.ResponseCode;
import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserExceptionCode implements ResponseCode {

	NOT_ENOUGH_TOKEN(false, HttpStatus.BAD_REQUEST, "리뷰 토큰이 부족합니다."),
    NOT_MATCH_CODE(false, HttpStatus.BAD_REQUEST, "이메일 인증 코드가 일치하지 않습니다."),
	NO_GITHUB_INFO(false, HttpStatus.BAD_REQUEST, "깃허브 정보가 없습니다."),
	NO_GITHUB_REPO(false, HttpStatus.BAD_REQUEST, "해당하는 Repository를 찾을 수 없습니다."),

    ALREADY_EXIST_NICKNAME(false, HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다");
	private final boolean success;
	private final HttpStatus status;
	private final String message;

}
