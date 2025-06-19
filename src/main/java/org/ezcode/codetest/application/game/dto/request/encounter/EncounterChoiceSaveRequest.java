package org.ezcode.codetest.application.game.dto.request.encounter;

import org.ezcode.codetest.common.validation.EnumValidator;
import org.ezcode.codetest.domain.game.model.encounter.EncounterChoice;
import org.ezcode.codetest.domain.game.model.encounter.RandomEncounter;
import org.ezcode.codetest.domain.game.model.encounter.RandomEncounterEffect;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "인카운터 선택지 저장 요청")
public record EncounterChoiceSaveRequest(

	@NotBlank(message = "인카운터 이름은 필수값입니다.")
	@Size(message = "인카운터 이름 입력은 최대 30글자 이내로만 가능합니다.", max = 30)
	@Schema(description = "인카운터 이름")
	String encounterName,

	@NotBlank(message = "인카운터 선택 이름은 필수값입니다.")
	@Schema(description = "인카운터 선택지 이름")
	@Size(message = "선택지 이름 입력은 최대 30글자 이내로만 가능합니다.", max = 30)
	String choiceName,

	@NotBlank(message = "인카운터 이펙트는 필수값입니다.")
	@Size(message = "효과 입력은 최대 30글자 이내로만 가능합니다.", max = 30)
	@EnumValidator(enumClass = RandomEncounterEffect.class)
	@Schema(description = "인카운터 선택지 효과")
	String randomEncounterEffect,

	@NotNull(message = "플레이어의 인카운터 선택지를 골라주세요.(true,false)")
	@Schema(description = "인카운터 선택지 결정(true:yes, false:no)")
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
