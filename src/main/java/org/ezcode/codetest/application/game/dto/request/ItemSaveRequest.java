package org.ezcode.codetest.application.game.dto.request;

import org.ezcode.codetest.domain.game.model.entity.Item;

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
