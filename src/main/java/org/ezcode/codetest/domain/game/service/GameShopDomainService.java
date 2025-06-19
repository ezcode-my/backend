package org.ezcode.codetest.domain.game.service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.ezcode.codetest.domain.game.exception.GameException;
import org.ezcode.codetest.domain.game.exception.GameExceptionCode;
import org.ezcode.codetest.domain.game.model.character.GameCharacter;
import org.ezcode.codetest.domain.game.model.skill.GameCharacterSkill;
import org.ezcode.codetest.domain.game.model.character.Inventory;
import org.ezcode.codetest.domain.game.model.item.Item;
import org.ezcode.codetest.domain.game.model.skill.Skill;
import org.ezcode.codetest.domain.game.model.item.ItemCategory;
import org.ezcode.codetest.domain.game.model.skill.SkillSlotType;
import org.ezcode.codetest.domain.game.repository.GameCharacterSkillRepository;
import org.ezcode.codetest.domain.game.repository.InventoryRepository;
import org.ezcode.codetest.domain.game.repository.ItemRepository;
import org.ezcode.codetest.domain.game.repository.SkillRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameShopDomainService {

	private final ItemRepository itemRepository;
	private final InventoryRepository inventoryRepository;
	private final SkillRepository skillRepository;
	private final GameCharacterSkillRepository characterSkillRepository;

	public Item gamblingNewWeapon(GameCharacter character) {

		character.useGoldForGamble();

		List<Item> weaponList = itemRepository.findAllByItemCategory(ItemCategory.WEAPON);

		int randomIndex = ThreadLocalRandom.current().nextInt(weaponList.size());

		Item item = weaponList.get(randomIndex);

		Inventory inventory = inventoryRepository.findByGameCharacterId(character.getId())
			.orElseThrow(() -> new GameException(GameExceptionCode.INVENTORY_NOT_FOUND));

		if (inventory.getWeapons().contains(item.getId())) {
			character.earnGold(25L);
			return item;
		}

		inventory.addItem(item.getItemType(), item.getId());

		return item;
	}

	public Item gamblingNewDefence(GameCharacter character) {

		character.useGoldForGamble();

		List<Item> defenceList = itemRepository.findAllByItemCategory(ItemCategory.DEFENCE);

		int randomIndex = ThreadLocalRandom.current().nextInt(defenceList.size());

		Item item = defenceList.get(randomIndex);

		Inventory inventory = inventoryRepository.findByGameCharacterId(character.getId())
			.orElseThrow(() -> new GameException(GameExceptionCode.INVENTORY_NOT_FOUND));

		if (inventory.getDefences().contains(item.getId())) {
			character.earnGold(25L);
			return item;
		}

		inventory.addItem(item.getItemType(), item.getId());

		return item;
	}

	public Item gamblingNewAccessory(GameCharacter character) {

		character.useGoldForGamble();

		List<Item> accessoryList = itemRepository.findAllByItemCategory(ItemCategory.ACCESSORY);

		int randomIndex = ThreadLocalRandom.current().nextInt(accessoryList.size());

		Item item = accessoryList.get(randomIndex);

		Inventory inventory = inventoryRepository.findByGameCharacterId(character.getId())
			.orElseThrow(() -> new GameException(GameExceptionCode.INVENTORY_NOT_FOUND));

		if (inventory.getAccessories().contains(item.getId())) {
			character.earnGold(25L);
			return item;
		}

		inventory.addItem(item.getItemType(), item.getId());

		return item;
	}

	public Skill gamblingNewSkill(GameCharacter character) {

		character.useGoldForGamble();

		List<Skill> skillList = skillRepository.findAll();

		int randomIndex = ThreadLocalRandom.current().nextInt(skillList.size());

		Skill skill = skillList.get(randomIndex);

		List<GameCharacterSkill> characterSkills = characterSkillRepository.findByCharacterId(character.getId());

		boolean alreadyHas = characterSkills.stream()
			.anyMatch(cs -> cs.getSkill().getId().equals(skill.getId()));

		if (alreadyHas) {
			character.earnGold(25L);
			return skill;
		}

		characterSkillRepository.save(
			GameCharacterSkill
				.builder()
				.skill(skill)
				.character(character)
				.slotType(SkillSlotType.BACKPACK)
				.build()
		);

		return skill;
	}

}
