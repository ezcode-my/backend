package org.ezcode.codetest.domain.game.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.ezcode.codetest.domain.game.exception.GameException;
import org.ezcode.codetest.domain.game.exception.GameExceptionCode;
import org.ezcode.codetest.domain.game.model.entity.GameCharacter;
import org.ezcode.codetest.domain.game.model.entity.Inventory;
import org.ezcode.codetest.domain.game.model.entity.Item;
import org.ezcode.codetest.domain.game.model.enums.ItemType;
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

		return itemRepository.findByIdIn(allIds);
	}

	public List<Item> loadEquippedItems(GameCharacter character) {

		String weaponId = character.getWeaponId();
		String defenceId = character.getDefenceId();
		String accessoryId = character.getAccessoryId();

		return itemRepository.findByIdIn(List.of(weaponId, defenceId, accessoryId));
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

		String oldItem = character.equipItem(type, checkFoundId);

		inventory.removeItem(type, checkFoundId);

		inventory.addItem(type, oldItem);
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
