package org.ezcode.codetest.application.game.dto.response.encounter;

import org.ezcode.codetest.domain.game.model.encounter.EncounterCategory;
import org.ezcode.codetest.domain.game.model.encounter.RandomEncounter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "인카운터 매칭 요청에 대한 응답")
public record MatchingEncounterResponse(

	@Schema(description = "인카운터 ID(jwt 토큰)")
	String encounterId,

	@Schema(description = "인카운터 카테고리")
	EncounterCategory encounterCategory,

	@Schema(description = "인카운터 이름")
	String name,

	@Schema(description = "인카운터 선택지 1")
	String choice1Text,

	@Schema(description = "인카운터 선택지 2")
	String choice2Text,

	@Schema(description = "인카운터 설명/묘사")
	String encounterText

) {
	public static MatchingEncounterResponse from(RandomEncounter encounter, String encounterIdToken) {

		return MatchingEncounterResponse.builder()
			.encounterCategory(encounter.getEncounterCategory())
			.encounterId(encounterIdToken)
			.name(encounter.getName())
			.encounterText(encounter.getEncounterText())
			.choice1Text(encounter.getChoice1Text())
			.choice2Text(encounter.getChoice2Text())
			.build();
	}
}
