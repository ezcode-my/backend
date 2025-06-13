package org.ezcode.codetest.application.game.dto.request;

import org.ezcode.codetest.domain.game.model.entity.Item;
import org.ezcode.codetest.domain.game.model.entity.Weapon;
import org.ezcode.codetest.domain.game.model.enums.WeaponType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class WeaponSaveRequest extends ItemSaveRequest {

	@NotBlank
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
		return Weapon.builder()
			.id(null)
			.type(wt)
			.grade(this.getGrade())
			.name(this.getName())
			.description(this.getDescription())
			.atk(atk)
			.speed(speed)
			.crit(crit)
			.stun(stun)
			.accuracy(accuracy)
			.build();
	}
}
