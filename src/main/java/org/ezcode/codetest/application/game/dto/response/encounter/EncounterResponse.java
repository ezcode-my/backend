package org.ezcode.codetest.application.game.dto.response.encounter;

import org.ezcode.codetest.domain.game.model.encounter.EncounterCategory;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "인카운터 조회 요청에 대한 응답")
public record EncounterResponse(

	@Schema(description = "인카운터 ID(jwt token)")
	Long encounterId,

	@Schema(description = "인카운터 카테고리")
	EncounterCategory encounterCategory,

	@Schema(description = "인카운터 이름")
	String name,

	@Schema(description = "인카운터 설명 및 묘사")
	String encounterText,

	@Schema(description = "인카운터 선택지 1")
	String choice1Text,

	@Schema(description = "인카운터 선택지 2")
	String choice2Text,

	@Schema(description = "현재 인카운터 활성 여부")
	boolean activated

) {

}
