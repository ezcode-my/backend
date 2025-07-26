package org.ezcode.codetest.application.submission.dto.response.submission;

import java.time.LocalDateTime;

import org.ezcode.codetest.domain.submission.model.entity.Submission;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "개별 제출 결과 응답")
public record SubmissionDetailResponse(

    @Schema(description = "제출 ID", example = "101")
    Long id,

    @Schema(description = "제출 언어", example = "Java(17)")
    String language,

    @Schema(description = "소스 코드", example = "System.out.println(\"Hello\");")
    String sourceCode,

    @Schema(description = "정답 여부", example = "true")
    boolean isCorrect,

    @Schema(description = "결과 메시지", example = "Accepted")
    String message,

    @Schema(description = "실행 시간 (s)", example = "0.129")
    Long executionTime,

    @Schema(description = "메모리 사용량 (KB)", example = "12196")
    Long memoryUsage,

    @Schema(description = "제출 시간", example = "2025-06-11T19:00:00")
    LocalDateTime submittedAt

) {
    public static SubmissionDetailResponse from(Submission submission) {
        return new SubmissionDetailResponse(
            submission.getId(),
            submission.getLanguageInfo(),
            submission.getCode(),
            submission.isCorrect(),
            submission.getMessage(),
            submission.getExecutionTime(),
            submission.getMemoryUsage(),
            submission.getCreatedAt().withNano(0)
        );
    }
}
