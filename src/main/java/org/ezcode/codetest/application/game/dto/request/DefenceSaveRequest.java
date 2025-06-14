package org.ezcode.codetest.application.game.dto.request;

import org.ezcode.codetest.domain.game.model.entity.Defence;
import org.ezcode.codetest.domain.game.model.entity.Item;
import org.ezcode.codetest.domain.game.model.enums.DefenceType;

import com.fasterxml.jackson.annotation.JsonTypeName;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@JsonTypeName("defence")
@Getter
public class DefenceSaveRequest extends ItemSaveRequest {

	@NotBlank
	private final String defenceType;

	@NotNull
	@Min(0)
	private final Integer def;

	@NotNull
	@Min(0)
	private final Integer speed;

	@NotNull
	@Min(0)
	private final Integer evasion;

	public DefenceSaveRequest(
		String grade,
		String name,
		String description,
		String defenceType,
		Integer def,
		Integer speed,
		Integer evasion
	) {
		super(grade, name, description);
		this.defenceType = defenceType;
		this.def = def;
		this.speed = speed;
		this.evasion = evasion;
	}

	@Override
	public Item toItem() {
		DefenceType dt = DefenceType.valueOf(defenceType.trim().toUpperCase());
		return Defence.builder()
			.id(null)
			.type(dt)
			.grade(getGrade())
			.name(getName())
			.description(getDescription())
			.def(getDef())
			.speed(getSpeed())
			.evasion(getEvasion())
			.build();
	}
}

