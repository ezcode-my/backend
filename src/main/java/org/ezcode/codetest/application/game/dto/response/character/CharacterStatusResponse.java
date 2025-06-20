package org.ezcode.codetest.application.game.dto.response.character;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.ezcode.codetest.application.game.dto.response.skill.SkillResponse;
import org.ezcode.codetest.application.game.dto.response.item.ItemResponse;
import org.ezcode.codetest.domain.game.model.character.CharacterRealStat;
import org.ezcode.codetest.domain.game.model.character.GameCharacter;
import org.ezcode.codetest.domain.game.model.character.Stat;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "캐릭터 스테이터스 조회 응답")
public record CharacterStatusResponse(

	@Schema(description = "캐릭터 이름")
	String name,

	@Schema(description = "캐릭터 가상 스텟")
	Map<Stat, Double> stats,

	@Schema(description = "캐릭터 실제 스텟(전투시 반영)")
	CharacterRealStat realStat,

	@Schema(description = "보유 골드")
	Long gold,

	@Schema(description = "장착한 아이템")
	List<ItemResponse> items,

	@Schema(description = "장착한 스킬")
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
