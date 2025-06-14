package org.ezcode.codetest.domain.game.model.entity;

import org.ezcode.codetest.domain.game.model.enums.ItemCategory;
import org.ezcode.codetest.domain.game.model.enums.ItemType;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Defence extends Item {

	private Integer def;
	private Integer speed;
	private Integer evasion;

	@Builder
	public Defence(
		String id,
		ItemType type,
		String grade,
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
