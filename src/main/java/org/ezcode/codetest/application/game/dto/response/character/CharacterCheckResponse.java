package org.ezcode.codetest.application.game.dto.response.character;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "캐릭터 생성 여부 확인 response")
public record CharacterCheckResponse(

	boolean isCharacterExist

) {
}
