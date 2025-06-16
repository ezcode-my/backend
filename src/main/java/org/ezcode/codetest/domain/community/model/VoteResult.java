package org.ezcode.codetest.domain.community.model;

import org.ezcode.codetest.domain.community.model.enums.VoteType;

public record VoteResult(

	VoteType voteType,

	Long upvoteCount,

	Long downvoteCount

) {
}
