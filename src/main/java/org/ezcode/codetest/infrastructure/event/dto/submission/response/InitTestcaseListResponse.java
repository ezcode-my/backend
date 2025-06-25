package org.ezcode.codetest.infrastructure.event.dto.submission.response;

import java.util.List;

import org.ezcode.codetest.application.submission.dto.event.payload.InitTestcaseListPayload;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "테스트케이스 초기화 WebSocket 응답 DTO")
public record InitTestcaseListResponse(

    @Schema(description = "테스트케이스 순번 (1부터 시작)", example = "1")
    int seqId,

    @Schema(description = "테스트케이스 입력 값", example = "1 2 3")
    String input,

    @Schema(description = "예상 출력 값", example = "6")
    String expectedOutput,

    @Schema(description = "채점 상태", example = "채점 중")
    String status

) {
    public static InitTestcaseListResponse from(InitTestcaseListPayload payload) {
        return new InitTestcaseListResponse(payload.seqId(), payload.input(), payload.expectedOutput(), payload.status());
    }

    public static List<InitTestcaseListResponse> mapToList(List<InitTestcaseListPayload> payload) {
        return payload.stream().map(InitTestcaseListResponse::from).toList();
    }
}
