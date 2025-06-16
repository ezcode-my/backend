package org.ezcode.codetest.application.community.dto.request;

import org.ezcode.codetest.domain.community.model.enums.VoteType;

public record VoteRequest(

	VoteType voteType

) {
}
