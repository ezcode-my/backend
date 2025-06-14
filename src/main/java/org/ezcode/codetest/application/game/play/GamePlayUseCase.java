package org.ezcode.codetest.application.game.play;

import java.util.List;

import org.ezcode.codetest.application.game.dto.mapper.GameMapper;
import org.ezcode.codetest.application.game.dto.response.CharacterStatusResponse;
import org.ezcode.codetest.application.game.dto.response.ItemGamblingResponse;
import org.ezcode.codetest.application.game.dto.response.ItemResponse;
import org.ezcode.codetest.domain.game.model.entity.GameCharacter;
import org.ezcode.codetest.domain.game.model.entity.Item;
import org.ezcode.codetest.domain.game.model.enums.ItemCategory;
import org.ezcode.codetest.domain.game.service.CharacterStatusDomainService;
import org.ezcode.codetest.domain.game.service.ItemShoppingDomainService;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.service.UserDomainService;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GamePlayUseCase {

	private final CharacterStatusDomainService characterService;
	private final ItemShoppingDomainService itemShoppingService;
	private final UserDomainService userDomainService;
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

		// TODO : 스킬 미구현 상태
		return CharacterStatusResponse.from(
			character,
			equippedItems.stream()
				.map(gameMapper::toItemResponse)
				.toList(),
			null
		);
	}

	@Transactional
	public ItemGamblingResponse gamblingForItem(Long userId, String itemCategory) {

		GameCharacter character = characterService.getGameCharacter(userId);

		Item newItem = switch (ItemCategory.valueOf(itemCategory.toUpperCase())) {
			case WEAPON -> itemShoppingService.gamblingNewWeapon(character);
			case DEFENCE -> itemShoppingService.gamblingNewDefence(character);
			case ACCESSORY -> itemShoppingService.gamblingNewAccessory(character);
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

		characterService.equipNewItem(character, itemName);
	}
}
