package org.ezcode.codetest.application.community.dto.response;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.*;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "VoteResponse", description = "추천 상태 응답 DTO")
public record VoteResponse(
	@Schema(
		description = "추천 상태 (추천되어 있으면 true, 취소되면 false)",
		example = "true",
		requiredMode = REQUIRED
	)
	boolean voteStatus
) {

	public static VoteResponse of(boolean voteStatus) {
		return new VoteResponse(voteStatus);
	}
}
