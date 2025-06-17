package org.ezcode.codetest.application.game.dto.request.item;

import org.ezcode.codetest.domain.game.model.item.Item;
import org.ezcode.codetest.domain.game.model.item.Weapon;
import org.ezcode.codetest.domain.game.model.item.Grade;
import org.ezcode.codetest.domain.game.model.item.WeaponType;

import com.fasterxml.jackson.annotation.JsonTypeName;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@JsonTypeName("weapon")
@Getter
public class WeaponSaveRequest extends ItemSaveRequest {

	@NotBlank
	@Pattern(
		regexp = "SHOT_GUN|RIFLE|PISTOL|LONG_SWORD|SHORT_SWORD|SPEAR|BOW",
		flags = Pattern.Flag.CASE_INSENSITIVE,
		message = "무기 타입은 SHOT_GUN, RIFLE, PISTOL, LONG_SWORD, SHORT_SWORD, SPEAR, BOW 중 하나여야 합니다."
	)
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
