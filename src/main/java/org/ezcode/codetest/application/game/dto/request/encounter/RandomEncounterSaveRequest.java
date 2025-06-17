package org.ezcode.codetest.application.game.dto.request.encounter;

import org.ezcode.codetest.domain.game.model.Encounter.RandomEncounter;
import org.ezcode.codetest.domain.game.model.Encounter.EncounterCategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record RandomEncounterSaveRequest(

	@NotBlank(message = "인카운터 카테고리는 필수입니다.")
	@Pattern(
		regexp = "^(BATTLE|MERCHANT|EVENT|EXPLORATION)$",
		flags  = Pattern.Flag.CASE_INSENSITIVE,
		message = "인카운터 카테고리는 BATTLE, MERCHANT, EVENT, EXPLORATION 중 하나여야 합니다."
	)
	String encounterCategory,

	@NotBlank(message = "이름은 필수입니다.")
	String name,

	@NotBlank(message = "설명란은 필수입니다.")
	String encounterText,

	@NotNull(message = "가중치 입력은 필수입니다.")
	Integer weight
) {

	public RandomEncounter toRandomEncounter() {

		return RandomEncounter.builder()
			.encounterCategory(EncounterCategory.valueOf(encounterCategory.trim().toUpperCase()))
			.activated(true)
			.name(name)
			.weight(weight)
			.encounterText(encounterText)
			.build();
	}
}
