package org.ezcode.codetest.application.game.dto.response.encounter;

public record MatchingResponse(

	boolean isEnemyStrongThanMe,

	String message,

	Long enemyId

) {

	public static MatchingResponse of(boolean isEnemyStrongThanMe, String message, Long enemyId) {

		return new MatchingResponse(isEnemyStrongThanMe, message, enemyId);
	}

}
