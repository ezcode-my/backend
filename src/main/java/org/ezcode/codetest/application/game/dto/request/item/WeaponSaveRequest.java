package org.ezcode.codetest.application.game.dto.request.item;

import org.ezcode.codetest.common.validation.EnumValidator;
import org.ezcode.codetest.domain.game.model.item.Item;
import org.ezcode.codetest.domain.game.model.item.Weapon;
import org.ezcode.codetest.domain.game.model.item.Grade;
import org.ezcode.codetest.domain.game.model.item.WeaponType;

import com.fasterxml.jackson.annotation.JsonTypeName;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@JsonTypeName("weapon")
@Getter
@Schema(description = "무기 저장 요청(해당 Dto 는 ItemSaveRequest 를 상속받기 때문에 ItemSaveRequest 의 필드도 같이보내야됩니다)")
public class WeaponSaveRequest extends ItemSaveRequest {

	@NotBlank(message = "무기 타입은 필수 입력입니다.")
	@Size(message = "무기 타입 입력은 최대 30글자 이내로만 가능합니다.", max = 30)
	@EnumValidator(enumClass = WeaponType.class)
	@Schema(description = "저장할 무기 타입")
	private final String weaponType;

	@NotNull(message = "공격력 스탯은 필수적으로 입력해야합니다.")
	@Min(message = "공격력 최솟값은 0 입니다.", value = 0)
	@Schema(description = "저장할 무기의 공격력 스탯")
	private final Integer atk;

	@NotNull(message = "스피드 스탯은 필수적으로 입력해야합니다.")
	@Min(message = "스피드 최솟값은 -10 입니다.", value = -10)
	@Schema(description = "저장할 무기의 스피드 스탯")
	private final Integer speed;

	@NotNull(message = "치명타 스탯은 필수적으로 입력해야합니다.")
	@Min(message = "치명타 최솟값은 -10 입니다.", value = -10)
	@Schema(description = "저장할 무기의 치명타 스탯")
	private final Integer crit;

	@NotNull(message = "스턴 스탯은 필수적으로 입력해야합니다.")
	@Min(message = "스턴 최솟값은 -10 입니다.", value = -10)
	@Schema(description = "저장할 무기의 기절 스탯")
	private final Integer stun;

	@NotNull(message = "명중 스탯은 필수적으로 입력해야합니다.")
	@Min(message = "명중 최솟값은 -10 입니다.", value = -10)
	@Schema(description = "저장할 무기의 명중 스탯")
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
