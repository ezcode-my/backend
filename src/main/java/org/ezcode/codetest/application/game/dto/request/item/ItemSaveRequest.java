package org.ezcode.codetest.application.game.dto.request.item;

import org.ezcode.codetest.common.validation.EnumValidator;
import org.ezcode.codetest.domain.game.model.item.Grade;
import org.ezcode.codetest.domain.game.model.item.Item;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@JsonTypeInfo(
	use = JsonTypeInfo.Id.NAME,
	include = JsonTypeInfo.As.PROPERTY,
	property = "type"
)
@JsonSubTypes({
	@JsonSubTypes.Type(value = WeaponSaveRequest.class, name = "weapon"),
	@JsonSubTypes.Type(value = DefenceSaveRequest.class, name = "defence"),
	@JsonSubTypes.Type(value = AccessorySaveRequest.class, name = "accessory")
})
@Getter
@Schema(description = "아이템 저장 요청(무기, 악세서리, 방어구의 최상위 클래스)")
public abstract class ItemSaveRequest {

	@NotBlank(message = "아이템 등급은 필수 입력입니다.")
	@Size(message = "아이템 등급 입력은 최대 30글자 이내로만 가능합니다.", max = 30)
	@EnumValidator(enumClass = Grade.class)
	@Schema(description = "저장할 아이템 등급")
	private final String grade;

	@NotBlank(message = "아이템 이름은 필수 입력입니다.")
	@Size(message = "아이템 이름 입력은 최대 30글자 이내로만 가능합니다.", max = 30)
	@Schema(description = "저장할 아이템 이름")
	private final String name;

	@NotBlank(message = "아이템 설명란은 필수 입력입니다.")
	@Size(message = "아이템 설명 입력은 최대 30글자 이내로만 가능합니다.", max = 30)
	@Schema(description = "저장할 아이템 설명/묘사")
	private final String description;

	protected ItemSaveRequest(String grade, String name, String description) {

		this.grade = grade;
		this.name = name;
		this.description = description;
	}

	public abstract Item toItem();
}
