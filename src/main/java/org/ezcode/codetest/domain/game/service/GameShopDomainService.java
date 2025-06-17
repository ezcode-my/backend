package org.ezcode.codetest.domain.game.service;

import java.util.List;
import java.util.Random;

import org.ezcode.codetest.domain.game.exception.GameException;
import org.ezcode.codetest.domain.game.exception.GameExceptionCode;
import org.ezcode.codetest.domain.game.model.Character.GameCharacter;
import org.ezcode.codetest.domain.game.model.skill.GameCharacterSkill;
import org.ezcode.codetest.domain.game.model.Character.Inventory;
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

		//TODO : 나중에 REDIS 에 캐싱해서 DB IO 를 줄이는 방법으로 수정 (레디스에 전체 리스트 사이즈랑 전체 필드 저장)
		List<Item> weaponList = itemRepository.findAllByItemCategory(ItemCategory.WEAPON);

		Random random = new Random();

		int randomIndex = random.nextInt(weaponList.size());

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

		Random random = new Random();

		int randomIndex = random.nextInt(defenceList.size());

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

		Random random = new Random();

		int randomIndex = random.nextInt(accessoryList.size());

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
		//TODO : 나중에 REDIS 에 캐싱해서 DB IO 를 줄이는 방법으로 수정 (레디스에 전체 리스트 사이즈랑 전체 필드 저장)

		Random random = new Random();

		int randomIndex = random.nextInt(skillList.size());

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

	public void sellingItemForGold(GameCharacter character) {

		character.earnGold(25L);
	}
}
