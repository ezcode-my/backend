package org.ezcode.codetest.domain.game.model.entity;

import java.util.ArrayList;
import java.util.List;

import org.ezcode.codetest.common.base.entity.BaseEntity;
import org.ezcode.codetest.domain.game.model.enums.Accessory;
import org.ezcode.codetest.domain.game.model.enums.Defence;
import org.ezcode.codetest.domain.game.model.enums.Weapon;

import co.elastic.clients.elasticsearch.xpack.usage.Base;
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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

	public void addWeapon(Weapon weapon) {

		weapons.add(weapon);
	}

	public void addDefence(Defence defence) {

		defences.add(defence);
	}

	public void addAccessories(Accessory accessory) {

		accessories.add(accessory);
	}

}
