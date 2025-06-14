package org.ezcode.codetest.domain.game.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.ezcode.codetest.domain.game.exception.GameException;
import org.ezcode.codetest.domain.game.exception.GameExceptionCode;
import org.ezcode.codetest.domain.game.model.entity.GameCharacter;
import org.ezcode.codetest.domain.game.model.entity.GameCharacterSkill;
import org.ezcode.codetest.domain.game.model.entity.Inventory;
import org.ezcode.codetest.domain.game.model.entity.Item;
import org.ezcode.codetest.domain.game.model.entity.Skill;
import org.ezcode.codetest.domain.game.model.enums.ItemType;
import org.ezcode.codetest.domain.game.model.enums.SkillSlotType;
import org.ezcode.codetest.domain.game.repository.GameCharacterRepository;
import org.ezcode.codetest.domain.game.repository.GameCharacterSkillRepository;
import org.ezcode.codetest.domain.game.repository.InventoryRepository;
import org.ezcode.codetest.domain.game.repository.ItemRepository;
import org.ezcode.codetest.domain.game.repository.SkillRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CharacterStatusDomainService {

	private final GameCharacterRepository characterRepository;
	private final InventoryRepository inventoryRepository;
	private final SkillRepository skillRepository;
	private final ItemRepository itemRepository;
	private final GameCharacterSkillRepository characterSkillRepository;

	private static final Integer ITEM_MAX_SLOT = 3;

	public GameCharacter createGameCharacter(GameCharacter character) {

		GameCharacter savedCharacter = characterRepository.save(character);
		inventoryRepository.save(new Inventory(savedCharacter));

		return savedCharacter;
	}

	public GameCharacter getGameCharacter(Long userId) {

		return characterRepository.findByUserId(userId)
			.orElseThrow(() -> new GameException(GameExceptionCode.CHARACTER_NOT_FOUND));
	}

	public List<Item> inventoryOpen(Long characterId) {

		Inventory inventory = inventoryRepository.findByGameCharacterId(characterId)
			.orElseThrow(() -> new GameException(GameExceptionCode.INVENTORY_NOT_FOUND));

		List<String> allIds = Stream.of(
				inventory.getWeapons(),
				inventory.getDefences(),
				inventory.getAccessories()
			)
			.flatMap(Collection::stream)
			.toList();

		List<Item> inventoryItems = itemRepository.findByIdIn(allIds);

		if (allIds.size() != inventoryItems.size()) {
			inventory.cleanUpdateInventory(inventoryItems);
		}

		return inventoryItems;
	}

	public List<Item> loadEquippedItems(GameCharacter character) {

		String weaponId = character.getWeaponId();
		String defenceId = character.getDefenceId();
		String accessoryId = character.getAccessoryId();

		List<Item> equippedItems = itemRepository.findByIdIn(List.of(weaponId, defenceId, accessoryId));

		if (equippedItems.size() == ITEM_MAX_SLOT) {
			return equippedItems;
		}

		character.unEquipAllItems();
		equippedItems.forEach(item -> character.equipItem(item.getItemType(), item.getId()));

		return equippedItems;
	}

	public List<Skill> loadEquippedSkills(GameCharacter character) {

		List<GameCharacterSkill> characterSkill =characterSkillRepository.findByCharacterIdAndEquipped(character.getId());

		return characterSkill.stream().map(GameCharacterSkill::getSkill).toList();
	}


	public void equipNewSkill(GameCharacter character, String skillName) {

		Skill skill = skillRepository.findByName(skillName)
			.orElseThrow(() -> new GameException(GameExceptionCode.SKILL_NOT_FOUND));

		List<GameCharacterSkill> characterSkills = characterSkillRepository.findByCharacterId(character.getId());

		GameCharacterSkill has = characterSkills.stream()
			.filter(cs -> cs.getSkill().getId().equals(skill.getId()))
			.findFirst()
			.orElseThrow(() -> new GameException(GameExceptionCode.SKILL_NOT_OWNED));

		if (has.getSlotType() == SkillSlotType.EQUIPPED) {
			throw new GameException(GameExceptionCode.SKILL_ALREADY_EQUIPPED);
		}

		long equippedCount = characterSkills.stream()
			.filter(cs -> cs.getSlotType() == SkillSlotType.EQUIPPED)
			.count();

		if (equippedCount >= 3) {
			throw new GameException(GameExceptionCode.SKILL_SLOT_FULL);
		}

		has.equipSkill();
	}

	public void equipNewItem(GameCharacter character, String itemName) {

		Inventory inventory = inventoryRepository.findByGameCharacterId(character.getId())
			.orElseThrow(() -> new GameException(GameExceptionCode.INVENTORY_NOT_FOUND));

		Item item = itemRepository.findByName(itemName)
			.orElseThrow(() -> new GameException(GameExceptionCode.ITEM_NOT_FOUND));

		ItemType type = item.getItemType();

		String foundItemId = inventory.findItem(type, item.getId());

		String checkFoundId = Optional.ofNullable(foundItemId)
			.orElseThrow(() -> new GameException(GameExceptionCode.ITEM_NOT_FOUND));

		character.equipItem(type, checkFoundId);
	}

	public void getNewItemToInventory(GameCharacter character, Item item) {

		Inventory inventory = inventoryRepository.findByGameCharacterId(character.getId())
			.orElseThrow(() -> new GameException(GameExceptionCode.INVENTORY_NOT_FOUND));

		inventory.addItem(item.getItemType(), item.getId());
	}

	public void removeItemFromInventory(GameCharacter character, Item item) {

		Inventory inventory = inventoryRepository.findByGameCharacterId(character.getId())
			.orElseThrow(() -> new GameException(GameExceptionCode.INVENTORY_NOT_FOUND));

		ItemType type = item.getItemType();

		String foundItemId = inventory.findItem(type, item.getId());

		String checkFoundId = Optional.ofNullable(foundItemId)
			.orElseThrow(() -> new GameException(GameExceptionCode.ITEM_NOT_FOUND));

		inventory.removeItem(type, checkFoundId);
	}

}
