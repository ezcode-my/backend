package org.ezcode.codetest.application.game.dto.request.item;

import org.ezcode.codetest.common.validation.EnumValidator;
import org.ezcode.codetest.domain.game.model.item.ItemCategory;

import jakarta.validation.constraints.NotBlank;

public record ItemGamblingRequest(

	@NotBlank(message = "아이템 타입을 결정해주세요(weapon, defence, accessory)")
	@EnumValidator(enumClass = ItemCategory.class)
	String itemCategory

) {
}
