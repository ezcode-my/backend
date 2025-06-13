package org.ezcode.codetest.application.game.dto.request;

import org.ezcode.codetest.domain.game.model.entity.Accessory;
import org.ezcode.codetest.domain.game.model.entity.Item;
import org.ezcode.codetest.domain.game.model.enums.AccessoryType;

import com.fasterxml.jackson.annotation.JsonTypeName;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@JsonTypeName("accessory")
@Getter
public class AccessorySaveRequest extends ItemSaveRequest {

	@NotBlank
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
		this.speed = (speed != null ? speed : 0);
		this.crit = (crit != null ? crit : 0);
		this.stun = (stun != null ? stun : 0);
		this.evasion = (evasion != null ? evasion : 0);
		this.accuracy = (accuracy != null ? accuracy : 0);
	}

	@Override
	public Item toItem() {
		AccessoryType at = AccessoryType.valueOf(accessoryType.trim().toUpperCase());
		return Accessory.builder()
			.id(null)
			.type(at)
			.grade(getGrade())
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
