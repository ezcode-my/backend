package org.ezcode.codetest.domain.game.model.item;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "items")
public class Defence extends Item {

	private Integer def;
	private Integer speed;
	private Integer evasion;

	@Builder
	public Defence(
		String id,
		ItemType type,
		Grade grade,
		String name,
		String description,
		Integer def,
		Integer speed,
		Integer evasion
	) {
		super(id, ItemCategory.DEFENCE, type, grade, name, description);
		this.def = def;
		this.speed = speed;
		this.evasion = evasion;
	}
}
