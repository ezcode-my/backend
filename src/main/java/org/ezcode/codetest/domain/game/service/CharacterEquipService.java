package org.ezcode.codetest.domain.game.service;

import static org.ezcode.codetest.domain.game.constant.GameConstants.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.ezcode.codetest.domain.game.exception.GameException;
import org.ezcode.codetest.domain.game.exception.GameExceptionCode;
import org.ezcode.codetest.domain.game.model.entity.GameCharacter;
import org.ezcode.codetest.domain.game.model.entity.GameCharacterSkill;
import org.ezcode.codetest.domain.game.model.entity.Inventory;
import org.ezcode.codetest.domain.game.model.entity.Item;
import org.ezcode.codetest.domain.game.model.entity.Skill;
import org.ezcode.codetest.domain.game.model.enums.ItemType;
import org.ezcode.codetest.domain.game.model.enums.SkillSlotType;
import org.ezcode.codetest.domain.game.repository.GameCharacterSkillRepository;
import org.ezcode.codetest.domain.game.repository.InventoryRepository;
import org.ezcode.codetest.domain.game.repository.ItemRepository;
import org.ezcode.codetest.domain.game.repository.SkillRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CharacterEquipService {

	private final InventoryRepository inventoryRepository;
	private final ItemRepository itemRepository;
	private final GameCharacterSkillRepository characterSkillRepository;
	private final SkillRepository skillRepository;

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

	public List<GameCharacterSkill> loadEquippedSkills(GameCharacter character) {

		List<GameCharacterSkill> characterSkill = characterSkillRepository.findByCharacterIdAndEquipped(
			character.getId());

		characterSkill.sort(Comparator.comparingInt(skill -> skill.getSlotType().getOrder()));

		return characterSkill;
	}

	public void equipDefaultItem(GameCharacter character, Inventory savedInventory, String itemName) {

		Item item = itemRepository.findByName(itemName)
			.orElseThrow(() -> new GameException(GameExceptionCode.ITEM_NOT_EXISTS));

		savedInventory.addItem(item.getItemType(), item.getId());
		character.equipItem(item.getItemType(), item.getId());
	}

	public void equipSkill(GameCharacter character, String skillName, Integer slotNumber) {

		Skill skill = skillRepository.findByName(skillName)
			.orElseThrow(() -> new GameException(GameExceptionCode.SKILL_NOT_FOUND));

		List<GameCharacterSkill> characterSkills = characterSkillRepository.findByCharacterId(character.getId());

		GameCharacterSkill has = characterSkills.stream()
			.filter(cs -> cs.getSkill().getId().equals(skill.getId()))
			.findFirst()
			.orElseThrow(() -> new GameException(GameExceptionCode.SKILL_NOT_OWNED));

		if (has.getSlotType().getOrder() < SkillSlotType.BACKPACK.getOrder()) {

			throw new GameException(GameExceptionCode.SKILL_ALREADY_EQUIPPED);
		}

		SkillSlotType wantedSlot = SkillSlotType.fromSlotNumber(slotNumber);

		characterSkills.stream()
			.filter(cs -> cs.getSlotType() == wantedSlot)
			.forEach(GameCharacterSkill::unEquipSkill);

		has.equipSkill(wantedSlot);
	}

	public void unEquipSkill(GameCharacter character, String skillName) {

		Skill skill = skillRepository.findByName(skillName)
			.orElseThrow(() -> new GameException(GameExceptionCode.SKILL_NOT_FOUND));

		List<GameCharacterSkill> characterSkills = characterSkillRepository.findByCharacterId(character.getId());

		GameCharacterSkill has = characterSkills.stream()
			.filter(cs -> cs.getSkill().getId().equals(skill.getId()))
			.findFirst()
			.orElseThrow(() -> new GameException(GameExceptionCode.SKILL_NOT_OWNED));

		if (has.getSlotType().getOrder() == SkillSlotType.BACKPACK.getOrder()) {

			throw new GameException(GameExceptionCode.SKILL_ALREADY_UNEQUIPPED);
		}

		has.unEquipSkill();
	}

	public void equipItem(GameCharacter character, String itemName) {

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

}
