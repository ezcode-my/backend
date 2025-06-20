package org.ezcode.codetest.domain.game.service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.ezcode.codetest.domain.game.exception.GameException;
import org.ezcode.codetest.domain.game.exception.GameExceptionCode;
import org.ezcode.codetest.domain.game.model.character.GameCharacter;
import org.ezcode.codetest.domain.game.model.item.Grade;
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

		int roll = ThreadLocalRandom.current().nextInt(100);

		Grade targetGrade = Grade.fromRoll(roll);

		List<Item> candidates = weaponList.stream()
			.filter(item -> item.getGrade() == targetGrade)
			.toList();

		if (candidates.isEmpty()) {
			candidates = weaponList;
		}

		int randomIndex = ThreadLocalRandom.current().nextInt(candidates.size());

		Item item = candidates.get(randomIndex);

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

		int roll = ThreadLocalRandom.current().nextInt(100);

		Grade targetGrade = Grade.fromRoll(roll);

		List<Item> candidates = defenceList.stream()
			.filter(item -> item.getGrade() == targetGrade)
			.toList();

		if (candidates.isEmpty()) {
			candidates = defenceList;
		}

		int randomIndex = ThreadLocalRandom.current().nextInt(candidates.size());

		Item item = candidates.get(randomIndex);

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

		int roll = ThreadLocalRandom.current().nextInt(100);

		Grade targetGrade = Grade.fromRoll(roll);

		List<Item> candidates = accessoryList.stream()
			.filter(item -> item.getGrade() == targetGrade)
			.toList();

		if (candidates.isEmpty()) {
			candidates = accessoryList;
		}

		int randomIndex = ThreadLocalRandom.current().nextInt(candidates.size());

		Item item = candidates.get(randomIndex);

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

		int roll = ThreadLocalRandom.current().nextInt(100);

		Grade targetGrade = Grade.fromRoll(roll);

		List<Skill> candidates = skillList.stream()
			.filter(s -> s.getGrade() == targetGrade)
			.toList();

		if (candidates.isEmpty()) {
			candidates = skillList;
		}

		int randomIndex = ThreadLocalRandom.current().nextInt(candidates.size());

		Skill picked = candidates.get(randomIndex);

		boolean alreadyHas = characterSkillRepository.existsByCharacterIdAndSkillId(character.getId(), picked.getId());

		if (alreadyHas) {
			character.earnGold(25L);
			return picked;
		}

		characterSkillRepository.save(
			GameCharacterSkill
				.builder()
				.skill(picked)
				.character(character)
				.slotType(SkillSlotType.BACKPACK)
				.build()
		);

		return picked;
	}

}
