package org.ezcode.codetest.application.submission.dto.response.submission;

import org.ezcode.codetest.application.submission.model.JudgeResult;
import org.ezcode.codetest.domain.submission.dto.AnswerEvaluation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "각 테스트케이스에 대한 채점 결과")
public record JudgeResultResponse(

    @Schema(description = "테스트케이스 통과 여부", example = "true")
    boolean isPassed,

    @Schema(description = "기댓값", example = "12")
    String expectedOutput,

    @Schema(description = "실제 출력값", example = "12")
    String actualOutput,

    @Schema(description = "실행 시간 (s)", example = "0.129")
    Double executionTime,

    @Schema(description = "메모리 사용량 (KB)", example = "12196")
    Long memoryUsage,

    @Schema(description = "결과 메시지", example = "Accepted")
    String message

) {
    public static JudgeResultResponse fromEvaluation(JudgeResult result, AnswerEvaluation evaluation) {
        return new JudgeResultResponse(
            evaluation.isPassed(),
            evaluation.expectedOutput(),
            evaluation.actualOutput(),
            result.executionTime(),
            result.memoryUsage(),
            result.message());
    }
}
