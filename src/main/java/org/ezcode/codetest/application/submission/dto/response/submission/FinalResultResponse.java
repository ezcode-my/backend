package org.ezcode.codetest.application.submission.dto.response.submission;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "최종 채점 결과 응답 DTO")
public class FinalResultResponse {

	@Schema(description = "전체 테스트케이스 수", example = "5")
	private final int totalCount;

	@Schema(description = "통과한 테스트케이스 수", example = "5")
	private final int passedCount;

	@Schema(description = "전체 통과 여부", example = "true")
	private final boolean isCorrect;

	@Schema(description = "메시지", example = "Accepted")
	private final String message;

	public FinalResultResponse(int totalCount, int passedCount, String message) {
		this.totalCount = totalCount;
		this.passedCount = passedCount;
		this.isCorrect = totalCount == passedCount;
		this.message = message;
	}
}
