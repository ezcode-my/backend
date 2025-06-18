package org.ezcode.codetest.domain.community.model;

import org.ezcode.codetest.domain.community.model.enums.VoteType;

public record VoteResult(

	VoteType voteType,

	// 이전 추천 상태. 알림 도배 방지 검증용
	VoteType prevVoteType,

	Long upvoteCount,

	Long downvoteCount

) {
}
