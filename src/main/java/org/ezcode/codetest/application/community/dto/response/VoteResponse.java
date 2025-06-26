package org.ezcode.codetest.application.community.dto.response;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.*;

import org.ezcode.codetest.domain.community.dto.VoteResult;
import org.ezcode.codetest.domain.community.model.enums.VoteType;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "VoteResponse", description = "추천 상태 응답 DTO")
public record VoteResponse(

	@Schema(
		description = "현재 투표 유형",
		example = "UP / DOWN / NONE",
		requiredMode = REQUIRED
	)
	VoteType voteType,

	@Schema(
		description = "총 UPVOTE 수",
		example = "10",
		requiredMode = REQUIRED
	)
	Long upvoteCount,

	@Schema(
		description = "총 DOWNVOTE 수",
		example = "2",
		requiredMode = REQUIRED
	)
	Long downvoteCount

) {

	public static VoteResponse from(VoteResult voteResult) {

		return new VoteResponse(
			voteResult.voteType(),
			voteResult.upvoteCount(),
			voteResult.downvoteCount()
		);
	}
}
