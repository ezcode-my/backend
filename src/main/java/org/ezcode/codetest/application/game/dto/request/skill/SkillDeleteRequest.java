package org.ezcode.codetest.application.game.dto.request.skill;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "스킬 삭제 요청")
public record SkillDeleteRequest(

	@NotBlank(message = "삭제하려는 스킬의 이름이 필요합니다.")
	@Size(message = "스킬 이름 입력은 최대 30글자 이내로만 가능합니다.", max = 30)
	@Schema(description = "삭제할 스킬 이름")
	String name

) {

}
