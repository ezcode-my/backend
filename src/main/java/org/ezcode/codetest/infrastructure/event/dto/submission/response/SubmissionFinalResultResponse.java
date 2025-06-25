package org.ezcode.codetest.infrastructure.event.dto.submission.response;

import org.ezcode.codetest.application.submission.dto.event.payload.SubmissionFinalResultPayload;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "최종 채점 결과 응답 DTO")
public record SubmissionFinalResultResponse(

    @Schema(description = "전체 테스트케이스 수", example = "5")
    int totalCount,

    @Schema(description = "통과한 테스트케이스 수", example = "5")
    int passedCount,

    @Schema(description = "전체 통과 여부", example = "true")
    boolean isCorrect,

    @Schema(description = "메시지", example = "Accepted")
    String message

) {
    public static SubmissionFinalResultResponse from(SubmissionFinalResultPayload payload) {
        return new SubmissionFinalResultResponse(
            payload.getTotalCount(),
            payload.getPassedCount(),
            payload.isCorrect(),
            payload.getMessage());
    }
}
