package org.ezcode.codetest.application.game.dto.request.encounter;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "인카운터 선택지 삭제 요청")
public record EncounterChoiceDeleteRequest(

	@Schema(description = "삭제할 인카운터 선택지 이름")
	@NotBlank(message = "삭제할 이름을 입력해주세요.")
	@Size(message = "이름 입력은 최대 30글자 이내로만 가능합니다.", max = 30)
	String name

) {
}
