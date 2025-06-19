package org.ezcode.codetest.application.game.dto.request.item;

import org.ezcode.codetest.common.validation.EnumValidator;
import org.ezcode.codetest.domain.game.model.item.ItemCategory;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "아이템 뽑기 요청")
public record ItemGamblingRequest(

	@NotBlank(message = "아이템 카테고리는 공백일 수 없습니다.")
	@Size(message = "아이템 카테고리 입력은 최대 30글자 이내로만 가능합니다.", max = 30)
	@EnumValidator(enumClass = ItemCategory.class)
	@Schema(description = "뽑기할 아이템 카테고리")
	String itemCategory

) {
}
