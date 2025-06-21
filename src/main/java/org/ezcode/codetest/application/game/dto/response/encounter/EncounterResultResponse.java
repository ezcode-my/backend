package org.ezcode.codetest.application.game.dto.response.encounter;

import org.ezcode.codetest.domain.game.model.encounter.EncounterHistory;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 인카운터 선택에 따른 결과 응답")
public record EncounterResultResponse(

	@Schema(description = "인카운터 로그(사용자 캐릭터 결말)")
	String log,

	@Schema(description = "해당 선택으로 긍정적 혹은 부정정 결과인지 셋팅")
	boolean isPositive

) {
	public static EncounterResultResponse from(EncounterHistory history) {

		return new EncounterResultResponse(history.getResultLog(), history.getIsPositive());
	}
}
