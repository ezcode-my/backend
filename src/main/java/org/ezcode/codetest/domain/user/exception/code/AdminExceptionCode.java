package org.ezcode.codetest.domain.user.exception.code;

import org.ezcode.codetest.common.base.exception.ResponseCode;
import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AdminExceptionCode implements ResponseCode {
    GRANT_ADMIN_SELF(false, HttpStatus.BAD_REQUEST, "본인에게 ADMIN 권한을 부여할 수 없습니다."),
    ALREADY_ADMIN_USER(false, HttpStatus.BAD_REQUEST, "이미 ADMIN 권한을 가진 유저입니다.");

    private final boolean success;
    private final HttpStatus status;
    private final String message;
}
