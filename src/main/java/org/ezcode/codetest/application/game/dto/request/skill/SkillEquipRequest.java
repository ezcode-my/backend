package org.ezcode.codetest.application.game.dto.request.skill;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "스킬 장착 요청")
public record SkillEquipRequest(

	@NotBlank(message = "장착하려는 스킬의 이름이 필요합니다.")
	@Size(message = "스킬 이름 입력은 최대 30글자 이내로만 가능합니다.", max = 30)
	@Schema(description = "장착할 스킬 이름")
	String name,

	@NotNull(message = "장착할 슬롯 넘버를 입력해주세요.")
	@Min(value = 1, message = "슬롯 넘버는 1 이상이어야 합니다.")
	@Max(value = 3, message = "슬롯 넘버는 3 이하이어야 합니다.")
	@Schema(description = "장착할 스킬 슬롯 숫자(1~3)")
	Integer slotNumber

) {
}
