package org.ezcode.codetest.application.game.dto.response;

public record ItemGamblingResponse(

	ItemResponse itemResponse,

	String message
) {

	public static ItemGamblingResponse from(ItemResponse itemResponse) {
		return new ItemGamblingResponse(itemResponse, "축하합니다! 뽑기에 성공하셨습니다.(중복아이템은 자동으로 골드로 환전됩니다)");
	}
}
