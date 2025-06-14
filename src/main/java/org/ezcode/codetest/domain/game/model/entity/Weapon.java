package org.ezcode.codetest.domain.game.model.entity;

import org.ezcode.codetest.domain.game.model.enums.Grade;
import org.ezcode.codetest.domain.game.model.enums.ItemCategory;
import org.ezcode.codetest.domain.game.model.enums.ItemType;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Weapon extends Item {

	private Integer atk;
	private Integer speed;
	private Integer crit;
	private Integer stun;
	private Integer accuracy;

	@Builder
	public Weapon(
		String id,
		ItemType type,
		Grade grade,
		String name,
		String description,
		Integer atk,
		Integer speed,
		Integer crit,
		Integer stun,
		Integer accuracy
	) {
		super(id, ItemCategory.WEAPON, type, grade, name, description);
		this.atk = atk;
		this.speed = speed;
		this.crit = crit;
		this.stun = stun;
		this.accuracy = accuracy;
	}
}
