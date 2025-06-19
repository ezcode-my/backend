package org.ezcode.codetest.application.game.dto.request.item;

import org.ezcode.codetest.common.validation.EnumValidator;
import org.ezcode.codetest.domain.game.model.item.Grade;
import org.ezcode.codetest.domain.game.model.item.Item;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jakarta.validation.constraints.NotBlank;
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
public abstract class ItemSaveRequest {

	@NotBlank
	@EnumValidator(enumClass = Grade.class)
	private final String grade;

	@NotBlank
	private final String name;

	@NotBlank
	private final String description;

	protected ItemSaveRequest(String grade, String name, String description) {

		this.grade = grade;
		this.name = name;
		this.description = description;
	}

	public abstract Item toItem();
}
