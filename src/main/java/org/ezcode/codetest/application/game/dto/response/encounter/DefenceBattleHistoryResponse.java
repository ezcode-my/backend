package org.ezcode.codetest.application.game.dto.response.encounter;

import java.time.LocalDateTime;

import org.ezcode.codetest.domain.game.model.encounter.BattleHistory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "캐릭터 방어 전투 결과 응답")
public record DefenceBattleHistoryResponse(

	@Schema(description = "공격자 캐릭터 닉네임")
	String attackerNickName,

	@Schema(description = "플레이어 캐릭터 닉네임")
	String PlayerNickName,

	@Schema(description = "전투 로그")
	String battleLog,

	@Schema(description = "플레이어 승패 여부(이겼을시 true, 패배시 false)")
	boolean isDefenderWin,

	@Schema(description = "PVP 가 일어난 시점")
	LocalDateTime battleCreatedAt

) {
	public static DefenceBattleHistoryResponse from(BattleHistory history) {
		return DefenceBattleHistoryResponse.builder()
			.attackerNickName(history.getAttacker().getName())
			.PlayerNickName(history.getDefender().getName())
			.battleLog(history.getBattleLog())
			.isDefenderWin(!history.getIsAttackerWin())
			.battleCreatedAt(history.getCreatedAt())
			.build();
	}
}
