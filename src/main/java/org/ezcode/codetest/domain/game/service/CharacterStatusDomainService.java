package org.ezcode.codetest.domain.game.service;

import static org.ezcode.codetest.domain.game.constant.GameConstants.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.ezcode.codetest.domain.game.exception.GameException;
import org.ezcode.codetest.domain.game.exception.GameExceptionCode;
import org.ezcode.codetest.domain.game.model.entity.GameCharacter;
import org.ezcode.codetest.domain.game.model.entity.GameCharacterSkill;
import org.ezcode.codetest.domain.game.model.entity.Inventory;
import org.ezcode.codetest.domain.game.model.entity.Item;
import org.ezcode.codetest.domain.game.repository.GameCharacterRepository;
import org.ezcode.codetest.domain.game.repository.InventoryRepository;
import org.ezcode.codetest.domain.game.repository.ItemRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CharacterStatusDomainService {

	private final GameCharacterRepository characterRepository;
	private final InventoryRepository inventoryRepository;
	private final ItemRepository itemRepository;
	private final CharacterEquipService characterLoadService;

	public GameCharacter createGameCharacter(GameCharacter character) {

		GameCharacter savedCharacter = characterRepository.save(character);
		Inventory savedInventory = inventoryRepository.save(new Inventory(savedCharacter));

		characterLoadService.equipDefaultItem(savedCharacter, savedInventory, DEFAULT_WEAPON);
		characterLoadService.equipDefaultItem(savedCharacter, savedInventory, DEFAULT_DEFENCE);
		characterLoadService.equipDefaultItem(savedCharacter, savedInventory, DEFAULT_ACCESSORY);

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

		return characterLoadService.loadEquippedItems(character);
	}

	public List<GameCharacterSkill> loadEquippedSkills(GameCharacter character) {

		return characterLoadService.loadEquippedSkills(character);
	}

	public void equipSkill(GameCharacter character, String skillName, Integer slotNumber) {

		characterLoadService.equipSkill(character, skillName, slotNumber);
	}

	public void UnEquipSkill(GameCharacter character, String skillName) {

		characterLoadService.UnEquipSkill(character, skillName);
	}

	public void equipItem(GameCharacter character, String itemName) {

		characterLoadService.equipItem(character, itemName);
	}

}
