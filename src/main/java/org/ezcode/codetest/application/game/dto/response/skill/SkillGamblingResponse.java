package org.ezcode.codetest.application.game.dto.response.skill;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "스킬 뽑기 요청에 대한 응답")
public record SkillGamblingResponse(

	@Schema(description = "스킬에 대한 전반적인 설명")
	SkillResponse response,

	@Schema(description = "뽑기 축하 메시지")
	String message

) {
	public static SkillGamblingResponse from(SkillResponse response) {

		return new SkillGamblingResponse(response, "축하합니다! 뽑기에 성공하셨습니다.(중복스킬은 자동으로 골드로 환전됩니다)");
	}
}
