package org.ezcode.codetest.application.game.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ItemDeleteRequest(

	@NotBlank(message = "삭제하려는 아이템 이름을 입력해주세요.")
	String name

) {
}
