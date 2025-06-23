package org.ezcode.codetest.application.game.dto.request.encounter;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "배틀 요청")
public record BattleRequest(

	@Schema(description = "배틀 토큰")
	@NotBlank(message = "배틀 토큰은 반드시 입력해야합니다.")
	String battleToken

) {
}

