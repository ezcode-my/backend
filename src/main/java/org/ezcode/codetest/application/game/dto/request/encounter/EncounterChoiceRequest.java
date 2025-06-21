package org.ezcode.codetest.application.game.dto.request.encounter;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "인카운터 선택지 결정 요청")
public record EncounterChoiceRequest(

	@Schema(description = "인카운터 토큰")
	@NotBlank(message = "인카운터 토큰은 반드시 입력해야합니다.")
	String encounterToken,

	@NotNull(message = "인카운터를 선택지를 선택해주세요.(true, false)")
	@Schema(description = "인카운터 선택지 결정(true:yes, false:no)")
	Boolean playerDecision

) {
}
