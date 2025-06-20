package org.ezcode.codetest.application.game.dto.request.encounter;

import org.ezcode.codetest.common.validation.EnumValidator;
import org.ezcode.codetest.domain.game.model.encounter.RandomEncounter;
import org.ezcode.codetest.domain.game.model.encounter.EncounterCategory;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "인카운터 저장 요청")
public record RandomEncounterSaveRequest(

	@NotBlank(message = "인카운터 카테고리는 필수입니다.")
	@EnumValidator(enumClass = EncounterCategory.class)
	@Size(message = "인카운터 카테고리 입력은 최대 30글자 이내로만 가능합니다.", max = 30)
	@Schema(description = "저장할 인카운터의 카테고리")
	String encounterCategory,

	@Size(message = "인카운터 이름 입력은 최대 30글자 이내로만 가능합니다.", max = 30)
	@NotBlank(message = "이름은 필수입니다.")
	@Schema(description = "저장할 인카운터의 이름")
	String name,

	@NotBlank(message = "설명란은 필수입니다.")
	@Schema(description = "저장할 인카운터의 설명 및 묘사")
	String encounterText

) {
	public RandomEncounter toRandomEncounter() {
		return RandomEncounter.builder()
			.encounterCategory(EncounterCategory.valueOf(encounterCategory.trim().toUpperCase()))
			.activated(true)
			.name(name)
			.encounterText(encounterText)
			.build();
	}
}
