package org.ezcode.codetest.application.game.dto.request.item;

import org.ezcode.codetest.common.validation.EnumValidator;
import org.ezcode.codetest.domain.game.model.item.Defence;
import org.ezcode.codetest.domain.game.model.item.Item;
import org.ezcode.codetest.domain.game.model.item.DefenceType;
import org.ezcode.codetest.domain.game.model.item.Grade;

import com.fasterxml.jackson.annotation.JsonTypeName;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@JsonTypeName("defence")
@Getter
@Schema(description = "방어구 저장 요청(해당 Dto 는 ItemSaveRequest 를 상속받기 때문에 ItemSaveRequest 의 필드도 같이보내야됩니다)")
public class DefenceSaveRequest extends ItemSaveRequest {

	@NotBlank(message = "방어구 타입은 필수 입력입니다.")
	@Size(message = "방어구 타입 입력은 최대 30글자 이내로만 가능합니다.", max = 30)
	@EnumValidator(enumClass = DefenceType.class)
	@Schema(description = "저장할 방어구 타입")
	private final String defenceType;

	@NotNull(message = "방어 스탯은 필수적으로 입력해야합니다.")
	@Min(message = "방어력 최솟값은 -10 입니다.", value = -10)
	@Schema(description = "저장할 방어구의 방어력 스탯")
	private final Integer def;

	@NotNull(message = "스피드 스탯은 필수적으로 입력해야합니다.")
	@Min(message = "스피드 최솟값은 -10 입니다.", value = -10)
	@Schema(description = "저장할 방어구의 스피드 스탯")
	private final Integer speed;

	@NotNull(message = "회피 스탯은 필수적으로 입력해야합니다.")
	@Min(message = "회피 최솟값은 -10 입니다.", value = -10)
	@Schema(description = "저장할 방어구의 회피 스탯")
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
		Grade grade = Grade.valueOf(getGrade().trim().toUpperCase());
		return Defence.builder()
			.id(null)
			.type(dt)
			.grade(grade)
			.name(getName())
			.description(getDescription())
			.def(getDef())
			.speed(getSpeed())
			.evasion(getEvasion())
			.build();
	}
}

