package org.ezcode.codetest.application.game.dto.request.encounter;

import org.ezcode.codetest.common.validation.EnumValidator;
import org.ezcode.codetest.domain.game.model.encounter.EncounterChoice;
import org.ezcode.codetest.domain.game.model.encounter.RandomEncounter;
import org.ezcode.codetest.domain.game.model.encounter.RandomEncounterEffect;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EncounterChoiceSaveRequest(

	@NotBlank(message = "인카운터 이름은 필수값입니다.")
	String encounterName,

	@NotBlank(message = "인카운터 선택 이름은 필수값입니다.")
	String choiceName,

	@NotBlank(message = "인카운터 이펙트는 필수값입니다.")
	@EnumValidator(enumClass = RandomEncounterEffect.class)
	String randomEncounterEffect,

	@NotNull(message = "플레이어의 인카운터 선택지를 골라주세요.(true,false)")
	Boolean playerDecision

) {
	public EncounterChoice toEncounterChoice(RandomEncounter encounter) {

		return EncounterChoice.builder()
			.encounter(encounter)
			.encounterEffect(RandomEncounterEffect.valueOf(randomEncounterEffect.trim().toUpperCase()))
			.name(choiceName)
			.playerDecision(playerDecision)
			.build();
	}
}
