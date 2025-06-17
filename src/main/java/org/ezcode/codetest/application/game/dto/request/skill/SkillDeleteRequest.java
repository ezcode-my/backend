package org.ezcode.codetest.application.game.dto.request.skill;

import jakarta.validation.constraints.NotBlank;

public record SkillDeleteRequest(

	@NotBlank(message = "삭제하려는 스킬의 이름이 필요합니다.")
	String name
) {

}
