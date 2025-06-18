package org.ezcode.codetest.domain.game.model.item;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "items")
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
