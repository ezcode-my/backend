package org.ezcode.codetest.application.game.dto.request.skill;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "스킬 장착 해제 요청")
public record SkillUnEquipRequest(

	@NotBlank(message = "장착 해제할 스킬 이름을 입력해주십시오.")
	@Size(message = "스킬 이름 입력은 최대 30글자 이내로만 가능합니다.", max = 30)
	@Schema(description = "장착 해제할 스킬 이름")
	String name

) {
}
