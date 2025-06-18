package org.ezcode.codetest.domain.game.model.item;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "items")
public class Accessory extends Item {

	private Integer speed;
	private Integer crit;
	private Integer stun;
	private Integer evasion;
	private Integer accuracy;

	@Builder
	public Accessory(
		String id,
		ItemType type,
		Grade grade,
		String name,
		String description,
		Integer speed,
		Integer crit,
		Integer stun,
		Integer evasion,
		Integer accuracy
	) {
		super(id, ItemCategory.ACCESSORY, type, grade, name, description);
		this.speed = speed;
		this.crit = crit;
		this.stun = stun;
		this.evasion = evasion;
		this.accuracy = accuracy;
	}
}
