package org.ezcode.codetest.application.game.dto.response.encounter;

public record MatchingBattleResponse(

	boolean isEnemyStrongThanMe,

	String message,

	Long enemyId

) {
	public static MatchingBattleResponse of(boolean isEnemyStrongThanMe, String message, Long enemyId) {

		return new MatchingBattleResponse(isEnemyStrongThanMe, message, enemyId);
	}
}
