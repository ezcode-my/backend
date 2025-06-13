package org.ezcode.codetest.application.game.dto.response;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.ezcode.codetest.domain.game.model.entity.CharacterRealStat;
import org.ezcode.codetest.domain.game.model.entity.GameCharacter;
import org.ezcode.codetest.domain.game.model.entity.Skill;
import org.ezcode.codetest.domain.game.model.enums.Stat;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CharacterStatusResponse(

	Map<Stat, Double> stats,

	CharacterRealStat realStat,

	Long gold,

	List<ItemResponse> items,

	List<Skill> skills
) {

	public static CharacterStatusResponse from(GameCharacter character, List<ItemResponse> items, List<Skill> skills) {

		return CharacterStatusResponse.builder()
			.realStat(character.getRealStat())
			.stats(Collections.unmodifiableMap(character.getStats()))
			.gold(character.getGold())
			.items(items)
			.skills(skills)
			.build();
	}

}
