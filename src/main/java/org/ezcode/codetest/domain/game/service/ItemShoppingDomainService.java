package org.ezcode.codetest.domain.game.service;

import org.ezcode.codetest.domain.game.model.entity.GameCharacter;
import org.ezcode.codetest.domain.game.model.enums.Accessory;
import org.ezcode.codetest.domain.game.model.enums.Defence;
import org.ezcode.codetest.domain.game.model.enums.Item;
import org.ezcode.codetest.domain.game.model.enums.Weapon;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemShoppingDomainService {

	public Weapon gamblingNewWeapon(GameCharacter character) {

		character.useGoldForGamble();

		return Item.randomItem(Weapon.class);
	}

	public Defence gamblingNewDefence(GameCharacter character) {

		character.useGoldForGamble();

		return Item.randomItem(Defence.class);
	}

	public Accessory gamblingNewAccessory(GameCharacter character) {

		character.useGoldForGamble();

		return Item.randomItem(Accessory.class);
	}

	public void sellingItemForGold(GameCharacter character) {

		character.earnGold(25L);
	}
}
