package org.ezcode.codetest.application.game.dto.request.item;

import org.ezcode.codetest.common.validation.EnumValidator;
import org.ezcode.codetest.domain.game.model.item.Accessory;
import org.ezcode.codetest.domain.game.model.item.Item;
import org.ezcode.codetest.domain.game.model.item.AccessoryType;
import org.ezcode.codetest.domain.game.model.item.Grade;

import com.fasterxml.jackson.annotation.JsonTypeName;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@JsonTypeName("accessory")
@Getter
@Schema(description = "악세서리 저장 요청(해당 Dto 는 ItemSaveRequest 를 상속받기 때문에 ItemSaveRequest 의 필드도 같이보내야됩니다)")
public class AccessorySaveRequest extends ItemSaveRequest {

	@NotBlank(message = "악세서리 타입은 필수 입력입니다.")
	@Size(message = "악세서리 타입 입력은 최대 30글자 이내로만 가능합니다.", max = 30)
	@EnumValidator(enumClass = AccessoryType.class)
	@Schema(description = "저장할 악세서리 타입")
	private final String accessoryType;

	@NotNull(message = "스피드 스탯은 필수적으로 입력해야합니다.")
	@Min(message = "스피드 최솟값은 -10 입니다.", value = -10)
	@Schema(description = "저장할 악세서리의 스피드 스탯")
	private final Integer speed;

	@NotNull(message = "치명타 스탯은 필수적으로 입력해야합니다.")
	@Min(message = "치명타 최솟값은 -10 입니다.", value = -10)
	@Schema(description = "저장할 악세서리의 치명타 스탯")
	private final Integer crit;

	@NotNull(message = "스턴 스탯은 필수적으로 입력해야합니다.")
	@Min(message = "스턴 최솟값은 -10 입니다.", value = -10)
	@Schema(description = "저장할 악세서리의 기절 스탯")
	private final Integer stun;

	@NotNull(message = "회피 스탯은 필수적으로 입력해야합니다.")
	@Min(message = "회피 최솟값은 -10 입니다.", value = -10)
	@Schema(description = "저장할 악세서리의 회피 스탯")
	private final Integer evasion;

	@NotNull(message = "명중 스탯은 필수적으로 입력해야합니다.")
	@Min(message = "명중 최솟값은 -10 입니다.", value = -10)
	@Schema(description = "저장할 악세서리의 명중 스탯")
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
