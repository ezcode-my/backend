package org.ezcode.codetest.application.game.dto.mapper;

import java.util.List;

import org.ezcode.codetest.application.game.dto.response.AccessoryResponse;
import org.ezcode.codetest.application.game.dto.response.CharacterStatusResponse;
import org.ezcode.codetest.application.game.dto.response.DefenceResponse;
import org.ezcode.codetest.application.game.dto.response.ItemResponse;
import org.ezcode.codetest.application.game.dto.response.SkillResponse;
import org.ezcode.codetest.application.game.dto.response.WeaponResponse;
import org.ezcode.codetest.domain.game.model.entity.Accessory;
import org.ezcode.codetest.domain.game.model.entity.CharacterRealStat;
import org.ezcode.codetest.domain.game.model.entity.Defence;
import org.ezcode.codetest.domain.game.model.entity.GameCharacter;
import org.ezcode.codetest.domain.game.model.entity.Item;
import org.ezcode.codetest.domain.game.model.entity.Skill;
import org.ezcode.codetest.domain.game.model.entity.Weapon;
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
	CharacterStatusResponse toCharacterStatusResponse(GameCharacter character, List<Item> items, List<Skill> skills);

	@AfterMapping
	default void applyItemStatsToRealStat(
		GameCharacter character,
		List<Item> items,
		List<Skill> skills,
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
