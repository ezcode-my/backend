package org.ezcode.codetest.application.game.dto.response.item;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "아이템 뽑기 요청에 대한 응답")
public record ItemGamblingResponse(

	@Schema(description = "아이템에 대한 전반적인 설명")
	ItemResponse itemResponse,

	@Schema(description = "뽑기 축하 메시지")
	String message

) {
	public static ItemGamblingResponse from(ItemResponse itemResponse) {
		return new ItemGamblingResponse(itemResponse, "축하합니다! 뽑기에 성공하셨습니다.(중복아이템은 자동으로 골드로 환전됩니다)");
	}
}
