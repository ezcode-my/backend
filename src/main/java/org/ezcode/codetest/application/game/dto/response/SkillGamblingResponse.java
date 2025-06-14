package org.ezcode.codetest.application.game.dto.response;

public record SkillGamblingResponse(

	SkillResponse response,

	String message
) {

	public static SkillGamblingResponse from(SkillResponse response) {

		return new SkillGamblingResponse(response, "축하합니다! 뽑기에 성공하셨습니다.(중복스킬은 자동으로 골드로 환전됩니다)");
	}

}
