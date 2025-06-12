package org.ezcode.codetest.domain.game.service;

import java.util.Optional;

import org.ezcode.codetest.domain.game.exception.GameException;
import org.ezcode.codetest.domain.game.exception.GameExceptionCode;
import org.ezcode.codetest.domain.game.model.entity.GameCharacter;
import org.ezcode.codetest.domain.game.model.entity.Inventory;
import org.ezcode.codetest.domain.game.model.enums.Item;
import org.ezcode.codetest.domain.game.repository.GameCharacterRepository;
import org.ezcode.codetest.domain.game.repository.InventoryRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CharacterStatusDomainService {

	private final GameCharacterRepository characterRepository;
	private final InventoryRepository inventoryRepository;

	public GameCharacter createGameCharacter(GameCharacter character) {

		GameCharacter savedCharacter = characterRepository.save(character);
		inventoryRepository.save(new Inventory(savedCharacter));

		return savedCharacter;
	}

	public GameCharacter getGameCharacter(Long userId) {

		return characterRepository.findByUserId(userId)
			.orElseThrow(() -> new GameException(GameExceptionCode.CHARACTER_NOT_FOUND));
	}

	public void equipNewItem(GameCharacter character, Item item) {

		Inventory inventory = inventoryRepository.findByGameCharacterId(character.getId())
			.orElseThrow(() -> new GameException(GameExceptionCode.INVENTORY_NOT_FOUND));

		Item found = inventory.findItem(item);

		Item newItem = Optional.ofNullable(found)
			.orElseThrow(() -> new GameException(GameExceptionCode.ITEM_NOT_FOUND));

		Item oldItem = character.equipItem(newItem);

		inventory.removeItem(newItem);
		inventory.addItem(oldItem);
	}

	public void getNewItemToInventory(GameCharacter character, Item item) {

		Inventory inventory = inventoryRepository.findByGameCharacterId(character.getId())
			.orElseThrow(() -> new GameException(GameExceptionCode.INVENTORY_NOT_FOUND));

		inventory.addItem(item);
	}

	public void removeItemFromInventory(GameCharacter character, Item item) {

		Inventory inventory = inventoryRepository.findByGameCharacterId(character.getId())
			.orElseThrow(() -> new GameException(GameExceptionCode.INVENTORY_NOT_FOUND));

		Item found = inventory.findItem(item);

		Item checkedFounded = Optional.ofNullable(found)
			.orElseThrow(() -> new GameException(GameExceptionCode.ITEM_NOT_FOUND));

		inventory.removeItem(checkedFounded);
	}

}
