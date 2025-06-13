package org.ezcode.codetest.application.game.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ItemGamblingRequest(

	@NotBlank(message = "아이템 타입을 결정해주세요(weapon, armor, accessory)")
	@Pattern(
		regexp = "weapon|armor|accessory",
		flags = Pattern.Flag.CASE_INSENSITIVE,
		message = "아이템 타입은 weapon, armor, accessory 중 하나여야 합니다."
	)
	String itemCategory

) {
}
