package org.ezcode.codetest.domain.game.model.encounter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RandomEncounterEffect {

	BOSS_BATTLE_BAD("보스와의 전투 발생했으나 이겨도 얻는 것이 없음"),
	BOSS_BATTLE_GOOD("보스와의 전투 발생, 이기면 보상을 받고 난이도가 쉬움"),

	GAMBLING_GOOD("도박성공 200골드 획득"),
	GAMBLING_BAD("도박실패 200골드 감소 0 원일때는 감소되지않음"),

	STAT_INCREASE("당신은 동굴에 숨겨진 영약을 발견 먹었는데 스탯이 상승함"),
	STAT_DECREASE("당신은 동굴에 숨겨진 영약을 발견 먹었는데 스탯이 감소함"),

	AMBUSH_BANDITS_FIGHT("매복한 도적과 싸움(이길 경우 골드 100획득, 전투경험으로 스탯상승, 질 경우 부상으로 영구 스탯하락)"),
	AMBUSH_BANDITS_ESCAPE("매복한 도적에게 도망(도망 실패시 고문을 당해서 영구 스탯하락, 가진돈의 1/2 분실)"),

	WILD_BEASTS_ESCAPE("야생 동물에게서 도주성공"),
	WILD_BEASTS_ATTACK("야생 동물에게 공격당함"),

	ANCIENT_RUINS_TREASURE("고대 유적에서 보물을 발견함, 랜덤 무기 획득(기존에 있는 아이템이면 골드 200환전)"),
	ANCIENT_RUINS_TRAP("고대 유적의 함정에 걸림, 골드 100 하락"),

	TREASURE_CACHE_FOUND("숨겨진 보물을 발견함 랜덤 방어구 획득(기존에 있는 아이템이면 골드 200환전)"),
	TREASURE_CACHE_EMPTY("빈 상자를 발견함 그냥 기분이 허망해지는 로그 추가");

	private final String description;

}
