package org.ezcode.codetest.application.game.dto.request.encounter;

import jakarta.validation.constraints.NotBlank;

public record EncounterChoiceDeleteRequest(

	@NotBlank(message = "삭제할 이름을 입력해주세요.")
	String name

) {
}
