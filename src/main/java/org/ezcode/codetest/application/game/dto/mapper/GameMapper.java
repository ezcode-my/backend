package org.ezcode.codetest.application.game.dto.mapper;

import java.util.List;

import org.ezcode.codetest.application.game.dto.response.item.AccessoryResponse;
import org.ezcode.codetest.application.game.dto.response.character.CharacterStatusResponse;
import org.ezcode.codetest.application.game.dto.response.item.DefenceResponse;
import org.ezcode.codetest.application.game.dto.response.item.ItemResponse;
import org.ezcode.codetest.application.game.dto.response.skill.SkillResponse;
import org.ezcode.codetest.application.game.dto.response.item.WeaponResponse;
import org.ezcode.codetest.domain.game.model.item.Accessory;
import org.ezcode.codetest.domain.game.model.character.CharacterRealStat;
import org.ezcode.codetest.domain.game.model.item.Defence;
import org.ezcode.codetest.domain.game.model.character.GameCharacter;
import org.ezcode.codetest.domain.game.model.skill.GameCharacterSkill;
import org.ezcode.codetest.domain.game.model.item.Item;
import org.ezcode.codetest.domain.game.model.item.Weapon;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.SubclassExhaustiveStrategy;
import org.mapstruct.SubclassMapping;

@Mapper(
	componentModel = "spring",
	subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION
)
public interface GameMapper {

	@SubclassMapping(target = WeaponResponse.class, source = Weapon.class)
	@SubclassMapping(target = DefenceResponse.class, source = Defence.class)
	@SubclassMapping(target = AccessoryResponse.class, source = Accessory.class)
	ItemResponse toItemResponse(Item item);

	WeaponResponse toItemResponse(Weapon weapon);
	DefenceResponse toItemResponse(Defence defence);
	AccessoryResponse toItemResponse(Accessory accessory);

	@Mapping(target = "realStat", ignore = true)
	@Mapping(target = "items", ignore = true)
	@Mapping(target = "skills", ignore = true)
	CharacterStatusResponse toCharacterStatusResponse(GameCharacter character, List<Item> items, List<GameCharacterSkill> skills);

	@AfterMapping
	default void applyItemStatsToRealStat(
		GameCharacter character,
		List<Item> items,
		List<GameCharacterSkill> skills,
		@MappingTarget CharacterStatusResponse.CharacterStatusResponseBuilder builder
	) {
		CharacterRealStat sum = new CharacterRealStat(character.getRealStat());

		sum.applyItemRealStat(items);

		List<ItemResponse> itemResponses = items.stream()
			.map(this::toItemResponse)
			.toList();

		List<SkillResponse> skillResponses = skills.stream()
			.map(SkillResponse::from)
			.toList();

		builder.realStat(sum).items(itemResponses).skills(skillResponses);
	}
}
