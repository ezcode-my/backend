package org.ezcode.codetest.infrastructure.event.dto.submission.response;

import org.ezcode.codetest.domain.submission.exception.code.SubmissionExceptionCode;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "WebSocket 채점 에러 응답")
public record ErrorWsResponse(

    @Schema(description = "에러 메시지", example = "테스트케이스 채점 시간이 초과되었습니다.")
    String message,

    @Schema(description = "에러 코드", example = "TESTCASE_TIMEOUT")
    String errorCode,

    @Schema(description = "HTTP 상태 코드", example = "504")
    int status

) {
    public static ErrorWsResponse from(SubmissionExceptionCode code) {
        return new ErrorWsResponse(code.getMessage(), code.name(), code.getStatus().value());
    }
}
