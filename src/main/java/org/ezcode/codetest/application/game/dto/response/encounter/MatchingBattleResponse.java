package org.ezcode.codetest.application.game.dto.response.encounter;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "전투 매칭 요청에 대한 응답")
public record MatchingBattleResponse(

	@Schema(description = "매칭된 상대방 플레이어가 플레이어보다 더 강한지, 대략적 가늠 기준")
	boolean isEnemyStrongThanMe,

	@Schema(description = "두 플레이어 간 조우했을 시 상황묘사")
	String message,

	@Schema(description = "적 플레이어의 캐릭터(jwtToken) ID")
	String enemyIdToken

) {
	public static MatchingBattleResponse of(boolean isEnemyStrongThanMe, String message, String enemyIdToken) {

		return new MatchingBattleResponse(isEnemyStrongThanMe, message, enemyIdToken);
	}
}
