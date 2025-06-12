package org.ezcode.codetest.domain.game.model.entity;

import java.util.ArrayList;
import java.util.List;

import org.ezcode.codetest.common.base.entity.BaseEntity;
import org.ezcode.codetest.domain.game.model.enums.Accessory;
import org.ezcode.codetest.domain.game.model.enums.Defence;
import org.ezcode.codetest.domain.game.model.enums.Item;
import org.ezcode.codetest.domain.game.model.enums.Weapon;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Entity
@Getter
public class Inventory extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "game_character_id", nullable = false)
	GameCharacter gameCharacter;

	@Enumerated(EnumType.STRING)
	@ElementCollection(fetch = FetchType.LAZY)
	List<Weapon> weapons = new ArrayList<>();

	@Enumerated(EnumType.STRING)
	@ElementCollection(fetch = FetchType.LAZY)
	List<Defence> defences = new ArrayList<>();

	@Enumerated(EnumType.STRING)
	@ElementCollection(fetch = FetchType.LAZY)
	List<Accessory> accessories = new ArrayList<>();

	public Inventory(GameCharacter gameCharacter) {

		this.gameCharacter = gameCharacter;
	}

	public void addItem(Item item) {

		if (item instanceof Weapon newWeapon)
			weapons.add(newWeapon);
		else if (item instanceof Defence newDefence)
			defences.add(newDefence);
		else if (item instanceof Accessory newAccessory)
			accessories.add(newAccessory);
	}

	public void removeItem(Item item) {
		if (item instanceof Weapon removeWeapon)
			weapons.remove(removeWeapon);
		else if (item instanceof Defence removeDefence)
			defences.remove(removeDefence);
		else if (item instanceof Accessory removeAccessory)
			accessories.remove(removeAccessory);
	}

	public Item findItem(Item item) {

		Item foundItem = null;

		if (item instanceof Weapon newWeapon) {
			foundItem = weapons.stream()
				.filter(w -> w.equals(newWeapon))
				.findFirst()
				.orElse(null);
		} else if (item instanceof Defence newDefence) {
			foundItem = defences.stream()
				.filter(d -> d.equals(newDefence))
				.findFirst()
				.orElse(null);
		} else if (item instanceof Accessory newAccessory) {
			foundItem = accessories.stream()
				.filter(a -> a.equals(newAccessory))
				.findFirst()
				.orElse(null);
		}
		return foundItem;
	}
}
