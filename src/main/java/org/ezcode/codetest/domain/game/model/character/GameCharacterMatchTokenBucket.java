package org.ezcode.codetest.domain.game.model.character;

import java.time.Duration;
import java.time.Instant;

import org.ezcode.codetest.domain.game.exception.GameException;
import org.ezcode.codetest.domain.game.exception.GameExceptionCode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "character_match_token_bucket")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GameCharacterMatchTokenBucket {

	private static final int MAX_BATTLE_TOKENS = 35;
	private static final int MAX_ENCOUNTER_TOKENS = 10;
	private static final Duration REFILL_INTERVAL = Duration.ofHours(6);

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "game_character_id", nullable = false, unique = true)
	private GameCharacter character;

	@Column(nullable = false)
	private int remainingBattleMatchTokens;

	@Column(nullable = false)
	private int remainingEncounterMatchTokens;

	@Column(nullable = false)
	private Instant lastRefillTime;

	public GameCharacterMatchTokenBucket(GameCharacter character) {

		this.character = character;
		remainingBattleMatchTokens = MAX_BATTLE_TOKENS;
		remainingEncounterMatchTokens = MAX_ENCOUNTER_TOKENS;
		lastRefillTime = Instant.now();
	}

	private void refillTokensIfNeeded() {
		Instant now = Instant.now();
		Duration sinceLastRefill = Duration.between(lastRefillTime, now);

		if (sinceLastRefill.compareTo(REFILL_INTERVAL) >= 0) {
			remainingBattleMatchTokens = MAX_BATTLE_TOKENS;
			remainingEncounterMatchTokens = MAX_ENCOUNTER_TOKENS;
			lastRefillTime = now;
		}
	}

	public void consumeBattleToken() {
		refillTokensIfNeeded();

		if (remainingBattleMatchTokens <= 0) {
			throw new GameException(GameExceptionCode.BATTLE_TOKEN_EXHAUSTED);
		}

		remainingBattleMatchTokens--;
	}

	public void consumeEncounterToken() {
		refillTokensIfNeeded();

		if (remainingEncounterMatchTokens <= 0) {
			throw new GameException(GameExceptionCode.ENCOUNTER_TOKEN_EXHAUSTED);
		}

		remainingEncounterMatchTokens--;
	}

}