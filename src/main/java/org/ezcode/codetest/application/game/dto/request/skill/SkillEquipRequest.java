package org.ezcode.codetest.application.game.dto.request.skill;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SkillEquipRequest(

	@NotBlank(message = "장착할 스킬 이름을 입력해주십시오.")
	String name,

	@NotNull(message = "장착할 슬롯 넘버를 입력해주세요.")
	@Min(value = 1, message = "슬롯 넘버는 1 이상이어야 합니다.")
	@Max(value = 3, message = "슬롯 넘버는 3 이하이어야 합니다.")
	Integer slotNumber

) {
}
