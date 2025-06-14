package org.ezcode.codetest.application.game.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ItemEquipRequest(

	@NotBlank(message = "장착할 아이템 이름을 입력해주십시오.")
	String name

) {
}
