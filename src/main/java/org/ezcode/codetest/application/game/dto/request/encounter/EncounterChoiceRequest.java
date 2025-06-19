package org.ezcode.codetest.application.game.dto.request.encounter;

import jakarta.validation.constraints.NotNull;

public record EncounterChoiceRequest(

	@NotNull(message = "인카운터를 선택지를 선택해주세요.(true, false)")
	Boolean playerDecision

) {
}
