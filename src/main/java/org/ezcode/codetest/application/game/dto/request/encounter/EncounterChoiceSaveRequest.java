package org.ezcode.codetest.application.game.dto.request.encounter;

import org.ezcode.codetest.domain.game.model.encounter.EncounterChoice;
import org.ezcode.codetest.domain.game.model.encounter.RandomEncounter;
import org.ezcode.codetest.domain.game.model.encounter.RandomEncounterEffect;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record EncounterChoiceSaveRequest(

	@NotBlank(message = "인카운터 이름은 필수값입니다.")
	String encounterName,

	@NotBlank(message = "인카운터 선택 이름은 필수값입니다.")
	String choiceName,

	@NotBlank(message = "인카운터 이펙트는 필수값입니다.")
	@Pattern(
		regexp = "^(BOSS_BATTLE_BAD"
			+ "|GAMBLING_GOOD|GAMBLING_BAD|BOSS_BATTLE_GOOD"
			+ "|STAT_INCREASE|STAT_DECREASE"
			+ "|AMBUSH_BANDITS_ESCAPE|AMBUSH_BANDITS_FIGHT"
			+ "|WILD_BEASTS_ESCAPE|WILD_BEASTS_ATTACK"
			+ "|ANCIENT_RUINS_TREASURE|ANCIENT_RUINS_TRAP"
			+ "|TREASURE_CACHE_FOUND|TREASURE_CACHE_EMPTY)$",
		flags  = Pattern.Flag.CASE_INSENSITIVE,
		message = "effect 는 RandomEncounterEffect 의 유효한 값이어야 합니다."
	)
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
