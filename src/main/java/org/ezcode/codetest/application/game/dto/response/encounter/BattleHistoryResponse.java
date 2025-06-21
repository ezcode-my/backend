package org.ezcode.codetest.application.game.dto.response.encounter;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "캐릭터 전투 후, 전투 결과 응답")
public record BattleHistoryResponse(

	@Schema(description = "플레이어 캐릭터 닉네임")
	String playerNickName,

	@Schema(description = "적 플레이어 캐릭터 닉네임")
	String enemyNickName,

	@Schema(description = "전투 로그")
	List<String> battleLog,

	@Schema(description = "플레이어 승패 여부(이겼을시 true, 패배시 false)")
	boolean isPlayerWin

) {
	public static BattleHistoryResponse of(
		String playerNickName,
		String enemyNickName,
		List<String> battleLog,
		boolean isPlayerWin
	) {
		return BattleHistoryResponse.builder()
			.playerNickName(playerNickName)
			.enemyNickName(enemyNickName)
			.battleLog(battleLog)
			.isPlayerWin(isPlayerWin)
			.build();
	}
}
