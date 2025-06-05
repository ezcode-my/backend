package org.ezcode.codetest.application.community.dto.response;

public record VoteResponse(

	// 추천 등록되면 true, 취소되면 false
	boolean voteStatus

) {
}
