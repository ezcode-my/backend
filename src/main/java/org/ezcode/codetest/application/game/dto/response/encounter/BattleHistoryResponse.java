package org.ezcode.codetest.application.game.dto.response.encounter;

import java.util.List;

import lombok.Builder;

@Builder
public record BattleHistoryResponse(

	String playerNickName,

	String enemyNickName,

	List<String> battleLog,

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
