package org.ezcode.codetest.application.game.dto.response.encounter;

import java.util.List;

import org.ezcode.codetest.domain.game.model.encounter.EncounterHistory;

public record EncounterResponse(

	List<String> log,

	boolean isPositive

) {

	public static EncounterResponse of(List<String> log, boolean isPositive) {

		return new EncounterResponse(log, isPositive);
	}

	public static EncounterResponse from(EncounterHistory history) {

		return new EncounterResponse(history.getResultLog(), history.getIsPositive());
	}

}
