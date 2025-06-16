package org.ezcode.codetest.application.game.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SkillUnEquipRequest(

	@NotBlank(message = "장착 해제할 스킬 이름을 입력해주십시오.")
	String name

) {
}
