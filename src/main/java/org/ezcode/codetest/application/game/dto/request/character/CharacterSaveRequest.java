package org.ezcode.codetest.application.game.dto.request.character;

import jakarta.validation.constraints.NotBlank;

public record CharacterSaveRequest(

	@NotBlank(message = "이름은 공백일 수 없습니다.")
	String name

) {
}
