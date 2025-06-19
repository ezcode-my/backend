package org.ezcode.codetest.application.game.dto.response.encounter;

import org.ezcode.codetest.domain.game.model.encounter.RandomEncounterEffect;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "인카운터 선택지 조회 요청에 대한 응답")
public record EncounterChoiceResponse(

	@Schema(description = "선택지 ID")
	Long id,

	@Schema(description = "선택지 이름")
	String name,

	@Schema(description = "해당 선택지의 이펙트(효과)")
	RandomEncounterEffect encounterEffect,

	@Schema(description = "플레이어 선택지에 따른 선택지(사용자가 true/false 입력시 해당 선택지가 발동)")
	boolean playerDecision

) {
}
