package org.ezcode.codetest.application.game.dto.request.item;

import org.ezcode.codetest.domain.game.model.item.Accessory;
import org.ezcode.codetest.domain.game.model.item.Item;
import org.ezcode.codetest.domain.game.model.item.AccessoryType;
import org.ezcode.codetest.domain.game.model.item.Grade;

import com.fasterxml.jackson.annotation.JsonTypeName;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@JsonTypeName("accessory")
@Getter
public class AccessorySaveRequest extends ItemSaveRequest {

	@NotBlank
	@Pattern(
		regexp = "GRAPHIC_CARD|USB|KEYBOARD|MOUSE|CPU|MEMORY|DISK",
		flags = Pattern.Flag.CASE_INSENSITIVE,
		message = "악세서리 타입은 GRAPHIC_CARD, USB, KEYBOARD, MOUSE, CPU, MEMORY, DISK 중 하나여야 합니다."
	)
	private final String accessoryType;

	@NotNull
	@Min(0)
	private final Integer speed;

	@NotNull @Min(0)
	private final Integer crit;

	@NotNull @Min(0)
	private final Integer stun;

	@NotNull @Min(0)
	private final Integer evasion;

	@NotNull @Min(0)
	private final Integer accuracy;

	public AccessorySaveRequest(
		String grade,
		String name,
		String description,
		String accessoryType,
		Integer speed,
		Integer crit,
		Integer stun,
		Integer evasion,
		Integer accuracy
	) {
		super(grade, name, description);
		this.accessoryType = accessoryType;
		this.speed = speed;
		this.crit = crit;
		this.stun = stun;
		this.evasion = evasion;
		this.accuracy = accuracy;
	}

	@Override
	public Item toItem() {
		AccessoryType at = AccessoryType.valueOf(accessoryType.trim().toUpperCase());
		Grade grade = Grade.valueOf(getGrade().trim().toUpperCase());
		return Accessory.builder()
			.id(null)
			.type(at)
			.grade(grade)
			.name(getName())
			.description(getDescription())
			.speed(getSpeed())
			.crit(getCrit())
			.stun(getStun())
			.evasion(getEvasion())
			.accuracy(getAccuracy())
			.build();
	}
}
