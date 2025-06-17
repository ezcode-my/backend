package org.ezcode.codetest.domain.game.model.Encounter;

import lombok.Getter;

@Getter
public enum RandomEncounterEffect {

	RANDOM_BATTLE("무작위 전투 발생", null),

	MERCHANT_GOOD_DEAL("상인과의 거래 성공", true),
	MERCHANT_BAD_DEAL("상인과의 거래 실패", false),

	STAT_INCREASE("스탯이 상승함", true),
	STAT_DECREASE("스탯이 감소함", false),

	AMBUSH_BANDITS_WIN("매복한 도적을 격퇴함", true),
	AMBUSH_BANDITS_LOSE("매복한 도적에게 패배함", false),

	WILD_BEASTS_ESCAPE("야생 동물에게서 도주 성공", true),
	WILD_BEASTS_ATTACK("야생 동물에게 공격당함", false),

	ANCIENT_RUINS_TREASURE("고대 유적에서 보물을 발견함", true),
	ANCIENT_RUINS_TRAP("고대 유적의 함정에 걸림", false),

	TREASURE_CACHE_FOUND("숨겨진 보물을 발견함", true),
	TREASURE_CACHE_EMPTY("빈 상자를 발견함", false);

	private final String description;
	private final Boolean success;

	RandomEncounterEffect(String description, Boolean success) {
		this.description = description;
		this.success = success;
	}
}
