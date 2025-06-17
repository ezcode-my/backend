package org.ezcode.codetest.application.game.dto.response.character;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.ezcode.codetest.application.game.dto.response.skill.SkillResponse;
import org.ezcode.codetest.application.game.dto.response.item.ItemResponse;
import org.ezcode.codetest.domain.game.model.Character.CharacterRealStat;
import org.ezcode.codetest.domain.game.model.Character.GameCharacter;
import org.ezcode.codetest.domain.game.model.Character.Stat;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CharacterStatusResponse(

	Map<Stat, Double> stats,

	CharacterRealStat realStat,

	Long gold,

	List<ItemResponse> items,

	List<SkillResponse> skills
) {

	public static CharacterStatusResponse from(GameCharacter character, List<ItemResponse> items, List<SkillResponse> skills) {

		return CharacterStatusResponse.builder()
			.realStat(character.getRealStat())
			.stats(Collections.unmodifiableMap(character.getStats()))
			.gold(character.getGold())
			.items(items)
			.skills(skills)
			.build();
	}

}
