package org.ezcode.codetest.infrastructure.event.dto.submission.response;

import org.ezcode.codetest.application.submission.dto.event.payload.TestcaseResultPayload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "각 테스트케이스에 대한 채점 결과")
public record JudgeResultResponse(

    @Schema(description = "테스트케이스 번호", example = "1")
    int seqId,

    @Schema(description = "테스트케이스 통과 여부", example = "true")
    boolean isPassed,

    @Schema(description = "실제 출력값", example = "12")
    String actualOutput,

    @Schema(description = "실행 시간 (ms)", example = "129")
    Long executionTime,

    @Schema(description = "메모리 사용량 (KB)", example = "12196")
    Long memoryUsage,

    @Schema(description = "결과 메시지", example = "Accepted")
    String message

) {
    public static JudgeResultResponse from(TestcaseResultPayload payload) {
        return new JudgeResultResponse(
            payload.seqId(),
            payload.isPassed(),
            payload.actualOutput(),
            payload.executionTime(),
            payload.memoryUsage(),
            payload.message()
        );
    }
}
