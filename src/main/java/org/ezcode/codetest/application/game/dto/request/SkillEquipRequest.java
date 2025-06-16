package org.ezcode.codetest.application.game.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SkillEquipRequest(

	@NotBlank(message = "장착할 스킬 이름을 입력해주십시오.")
	String name,

	@NotNull(message = "장착할 슬롯 넘버를 입력해주세요.")
	Integer slotNumber

) {
}
