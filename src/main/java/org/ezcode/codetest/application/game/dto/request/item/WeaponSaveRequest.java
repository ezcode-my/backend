package org.ezcode.codetest.application.game.dto.request.item;

import org.ezcode.codetest.common.validation.EnumValidator;
import org.ezcode.codetest.domain.game.model.item.Item;
import org.ezcode.codetest.domain.game.model.item.Weapon;
import org.ezcode.codetest.domain.game.model.item.Grade;
import org.ezcode.codetest.domain.game.model.item.WeaponType;

import com.fasterxml.jackson.annotation.JsonTypeName;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@JsonTypeName("weapon")
@Getter
public class WeaponSaveRequest extends ItemSaveRequest {

	@NotBlank(message = "무기 타입은 공백일 수 없습니다.")
	@EnumValidator(enumClass = WeaponType.class)
	private final String weaponType;

	@NotNull
	@Min(0)
	private final Integer atk;

	@NotNull
	@Min(0)
	private final Integer speed;

	@NotNull
	@Min(0)
	private final Integer crit;

	@NotNull
	@Min(0)
	private final Integer stun;

	@NotNull
	@Min(0)
	private final Integer accuracy;

	public WeaponSaveRequest(
		String grade,
		String name,
		String description,
		String weaponType,
		Integer atk,
		Integer speed,
		Integer crit,
		Integer stun,
		Integer accuracy
	) {
		super(grade, name, description);
		this.weaponType = weaponType;
		this.atk = atk;
		this.speed = speed;
		this.crit = crit;
		this.stun = stun;
		this.accuracy = accuracy;
	}

	public Item toItem() {
		WeaponType wt = WeaponType.valueOf(weaponType.trim().toUpperCase());
		Grade grade = Grade.valueOf(getGrade().trim().toUpperCase());
		return Weapon.builder()
			.id(null)
			.type(wt)
			.grade(grade)
			.name(getName())
			.description(getDescription())
			.atk(atk)
			.speed(speed)
			.crit(crit)
			.stun(stun)
			.accuracy(accuracy)
			.build();
	}
}
