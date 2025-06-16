package org.ezcode.codetest.application.community.dto.response;

import org.ezcode.codetest.domain.community.model.VoteResult;
import org.ezcode.codetest.domain.community.model.enums.VoteType;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "VoteResponse", description = "추천 상태 응답 DTO")
public record VoteResponse(

	VoteType voteType,

	Long upvoteCount,

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
