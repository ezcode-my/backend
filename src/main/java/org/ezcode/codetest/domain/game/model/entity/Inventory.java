package org.ezcode.codetest.domain.game.model.entity;

import java.util.ArrayList;
import java.util.List;

import org.ezcode.codetest.common.base.entity.BaseEntity;
import org.ezcode.codetest.domain.game.model.enums.AccessoryType;
import org.ezcode.codetest.domain.game.model.enums.DefenceType;
import org.ezcode.codetest.domain.game.model.enums.ItemType;
import org.ezcode.codetest.domain.game.model.enums.WeaponType;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Inventory extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "game_character_id", nullable = false)
	private GameCharacter gameCharacter;

	@ElementCollection(fetch = FetchType.LAZY)
	private List<String> weapons = new ArrayList<>();

	@ElementCollection(fetch = FetchType.LAZY)
	private List<String> defences = new ArrayList<>();

	@ElementCollection(fetch = FetchType.LAZY)
	private List<String> accessories = new ArrayList<>();

	public Inventory(GameCharacter gameCharacter) {

		this.gameCharacter = gameCharacter;
	}

	public void addItem(ItemType item, String itemId) {

		if (item instanceof WeaponType wt && wt != WeaponType.NOTHING)
			weapons.add(itemId);
		else if (item instanceof DefenceType dt && dt != DefenceType.NOTHING)
			defences.add(itemId);
		else if (item instanceof AccessoryType at && at != AccessoryType.NOTHING)
			accessories.add(itemId);
	}

	public void removeItem(ItemType item, String itemId) {

		if (item instanceof WeaponType)
			weapons.remove(itemId);
		else if (item instanceof DefenceType)
			defences.remove(itemId);
		else if (item instanceof AccessoryType)
			accessories.remove(itemId);
	}

	public void cleanUpdateInventory(List<Item> items) {

		weapons.clear();
		defences.clear();
		accessories.clear();

		items.forEach(item -> {
			if (item.getItemType() instanceof WeaponType wt && wt != WeaponType.NOTHING)
				weapons.add(item.getId());
			else if (item.getItemType() instanceof DefenceType dt && dt != DefenceType.NOTHING)
				defences.add(item.getId());
			else if (item.getItemType() instanceof AccessoryType at && at != AccessoryType.NOTHING)
				accessories.add(item.getId());
		});
	}

	public String findItem(ItemType item, String itemId) {

		String foundItem = null;

		if (item instanceof WeaponType) {
			foundItem = weapons.stream()
				.filter(w -> w.equals(itemId))
				.findFirst()
				.orElse(null);
		} else if (item instanceof DefenceType) {
			foundItem = defences.stream()
				.filter(d -> d.equals(itemId))
				.findFirst()
				.orElse(null);
		} else if (item instanceof AccessoryType) {
			foundItem = accessories.stream()
				.filter(a -> a.equals(itemId))
				.findFirst()
				.orElse(null);
		}
		return foundItem;
	}
}
