package org.ezcode.codetest.domain.submission.exception.code;

import org.ezcode.codetest.common.base.exception.ResponseCode;
import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GitHubExceptionCode implements ResponseCode {

    INVALID_ACCESS_TOKEN(false, HttpStatus.UNAUTHORIZED, "유효하지 않은 GitHub 액세스 토큰입니다."),
    AUTHENTICATION_FAILED(false, HttpStatus.UNAUTHORIZED, "GitHub 인증에 실패했습니다."),
    PERMISSION_DENIED(false, HttpStatus.FORBIDDEN, "해당 리소스에 대한 권한이 없습니다."),

    REPOSITORY_NOT_FOUND(false, HttpStatus.NOT_FOUND, "지정한 리포지토리를 찾을 수 없습니다."),
    BRANCH_NOT_FOUND(false, HttpStatus.NOT_FOUND, "지정한 브랜치를 찾을 수 없습니다."),
    SOURCE_FILE_NOT_FOUND(false, HttpStatus.NOT_FOUND, "소스 파일을 찾을 수 없습니다."),

    TREE_CREATION_FAILED(false, HttpStatus.INTERNAL_SERVER_ERROR, "새 Git 트리 생성에 실패했습니다."),
    COMMIT_CREATION_FAILED(false, HttpStatus.INTERNAL_SERVER_ERROR, "커밋 생성에 실패했습니다."),
    BRANCH_UPDATE_FAILED(false, HttpStatus.INTERNAL_SERVER_ERROR, "브랜치 포인터 업데이트(푸시)에 실패했습니다."),

    RATE_LIMIT_EXCEEDED(false, HttpStatus.TOO_MANY_REQUESTS, "GitHub API 호출 한도를 초과했습니다."),
    NETWORK_ERROR(false, HttpStatus.SERVICE_UNAVAILABLE, "GitHub 서버와의 통신 중 네트워크 오류가 발생했습니다."),

    UNKNOWN_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 GitHub 연동 오류가 발생했습니다.");

    private final boolean success;
    private final HttpStatus status;
    private final String message;
}
