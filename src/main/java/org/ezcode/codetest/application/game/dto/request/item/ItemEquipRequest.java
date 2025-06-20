package org.ezcode.codetest.application.game.dto.request.item;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "아이템 장착 요청")
public record ItemEquipRequest(

	@Size(message = "아이템 이름 입력은 최대 30글자 이내로만 가능합니다.", max = 30)
	@NotBlank(message = "장착할 아이템 이름을 입력해주세요.")
	@Schema(description = "장착할 아이템 이름")
	String name

) {
}
