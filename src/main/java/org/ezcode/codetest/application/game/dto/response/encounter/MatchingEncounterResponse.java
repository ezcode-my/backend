package org.ezcode.codetest.application.game.dto.response.encounter;

import org.ezcode.codetest.domain.game.model.encounter.EncounterCategory;
import org.ezcode.codetest.domain.game.model.encounter.RandomEncounter;

import lombok.Builder;

@Builder
public record MatchingEncounterResponse(

	Long id,

	EncounterCategory encounterCategory,

	String name,

	String encounterText

) {
	public static MatchingEncounterResponse from(RandomEncounter encounter) {

		return MatchingEncounterResponse.builder()
			.encounterCategory(encounter.getEncounterCategory())
			.id(encounter.getId())
			.name(encounter.getName())
			.encounterText(encounter.getEncounterText())
			.build();
	}
}
