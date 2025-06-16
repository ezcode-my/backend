package org.ezcode.codetest.application.game.dto.request;

import org.ezcode.codetest.domain.game.model.entity.EncounterChoice;
import org.ezcode.codetest.domain.game.model.entity.RandomEncounter;
import org.ezcode.codetest.domain.game.model.enums.RandomEncounterEffect;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EncounterChoiceSaveRequest(

	@NotBlank(message = "인카운터 이름은 필수값입니다.")
	String encounterName,

	@NotBlank(message = "인카운터 선택 이름은 필수값입니다.")
	String choiceName,

	@NotBlank(message = "인카운터 선택 결과값은 필수값입니다.")
	String resultText,

	@NotBlank(message = "인카운터 이펙트는 필수값입니다.")
	@Pattern(
		regexp = "^(RANDOM_BATTLE"
			+ "|MERCHANT_GOOD_DEAL|MERCHANT_BAD_DEAL"
			+ "|STAT_INCREASE|STAT_DECREASE"
			+ "|AMBUSH_BANDITS_WIN|AMBUSH_BANDITS_LOSE"
			+ "|WILD_BEASTS_ESCAPE|WILD_BEASTS_ATTACK"
			+ "|ANCIENT_RUINS_TREASURE|ANCIENT_RUINS_TRAP"
			+ "|TREASURE_CACHE_FOUND|TREASURE_CACHE_EMPTY)$",
		message = "effect 는 RandomEncounterEffect 의 유효한 값이어야 합니다."
	)
	String randomEncounterEffect

) {

	public EncounterChoice toEncounterChoice(RandomEncounter encounter) {

		return EncounterChoice.builder()
			.encounter(encounter)
			.encounterEffect(RandomEncounterEffect.valueOf(randomEncounterEffect.trim().toUpperCase()))
			.resultText(resultText)
			.name(choiceName)
			.build();
	}

}
