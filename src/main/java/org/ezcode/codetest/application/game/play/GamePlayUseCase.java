package org.ezcode.codetest.application.game.play;

import java.util.List;

import org.ezcode.codetest.application.game.dto.mapper.GameMapper;
import org.ezcode.codetest.application.game.dto.request.SkillEquipRequest;
import org.ezcode.codetest.application.game.dto.request.SkillUnEquipRequest;
import org.ezcode.codetest.application.game.dto.response.CharacterStatusResponse;
import org.ezcode.codetest.application.game.dto.response.ItemGamblingResponse;
import org.ezcode.codetest.application.game.dto.response.ItemResponse;
import org.ezcode.codetest.application.game.dto.response.SkillGamblingResponse;
import org.ezcode.codetest.application.game.dto.response.SkillResponse;
import org.ezcode.codetest.domain.game.model.entity.GameCharacter;
import org.ezcode.codetest.domain.game.model.entity.GameCharacterSkill;
import org.ezcode.codetest.domain.game.model.entity.Item;
import org.ezcode.codetest.domain.game.model.entity.Skill;
import org.ezcode.codetest.domain.game.model.enums.ItemCategory;
import org.ezcode.codetest.domain.game.model.vo.BattleLog;
import org.ezcode.codetest.domain.game.service.CharacterStatusDomainService;
import org.ezcode.codetest.domain.game.service.GameEncounterDomainService;
import org.ezcode.codetest.domain.game.service.GameShopDomainService;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.service.UserDomainService;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GamePlayUseCase {

	private final CharacterStatusDomainService characterService;
	private final GameShopDomainService gameShopService;
	private final UserDomainService userDomainService;
	private final GameEncounterDomainService encounterDomainService;
	private final GameMapper gameMapper;

	@Transactional
	public void createCharacter(String email) {

		User user = userDomainService.getUser(email);

		characterService.createGameCharacter(new GameCharacter(user));
	}

	@Transactional
	public CharacterStatusResponse characterStatusOpen(Long userId) {

		GameCharacter character = characterService.getGameCharacter(userId);

		List<Item> equippedItems = characterService.loadEquippedItems(character);

		List<GameCharacterSkill> equippedSkills = characterService.loadEquippedSkills(character);

		return gameMapper.toCharacterStatusResponse(character, equippedItems, equippedSkills);
	}

	@Transactional
	public ItemGamblingResponse gamblingForItem(Long userId, String itemCategory) {

		GameCharacter character = characterService.getGameCharacter(userId);

		Item newItem = switch (ItemCategory.valueOf(itemCategory.toUpperCase())) {
			case WEAPON -> gameShopService.gamblingNewWeapon(character);
			case DEFENCE -> gameShopService.gamblingNewDefence(character);
			case ACCESSORY -> gameShopService.gamblingNewAccessory(character);
		};

		return ItemGamblingResponse.from(gameMapper.toItemResponse(newItem));
	}

	@Transactional
	public List<ItemResponse> inventoryOpen(Long userId) {

		GameCharacter character = characterService.getGameCharacter(userId);

		List<Item> inventoryItems = characterService.inventoryOpen(character.getId());

		return inventoryItems.stream().map(gameMapper::toItemResponse).toList();
	}

	@Transactional
	public void equipItem(Long userId, String itemName) {

		GameCharacter character = characterService.getGameCharacter(userId);

		characterService.equipItem(character, itemName);
	}

	@Transactional
	public void equipSkill(Long userId, SkillEquipRequest request) {

		GameCharacter character = characterService.getGameCharacter(userId);

		characterService.equipSkill(character, request.name(), request.slotNumber());
	}

	@Transactional
	public void unEquipSkill(Long userId, SkillUnEquipRequest request) {

		GameCharacter character = characterService.getGameCharacter(userId);

		characterService.UnEquipSkill(character, request.name());
	}

	@Transactional
	public SkillGamblingResponse gamblingForSkill(Long userId) {

		GameCharacter character = characterService.getGameCharacter(userId);

		Skill skill = gameShopService.gamblingNewSkill(character);

		return SkillGamblingResponse.from(SkillResponse.from(skill));
	}

	@Transactional
	public BattleLog battle(Long playerId, Long enemyId) {

		GameCharacter playerCharacter = characterService.getGameCharacter(playerId);
		GameCharacter enemyCharacter = characterService.getGameCharacter(enemyId);

		return encounterDomainService.battle(playerCharacter, enemyCharacter);
	}

}
