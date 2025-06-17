package org.ezcode.codetest.application.community.dto.request;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.*;

import org.ezcode.codetest.domain.community.model.enums.VoteType;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "VoteRequest", description = "추천 요청 DTO")
public record VoteRequest(

	@Schema(
		description = "투표 유형 (UP / DOWN / NONE)",
		example = "UP",
		requiredMode = REQUIRED
	)
	VoteType voteType

) {
}
