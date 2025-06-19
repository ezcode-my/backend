package org.ezcode.codetest.domain.game.strategy.encounter.encounterstrategyimpl;

import org.ezcode.codetest.domain.game.model.character.CharacterRealStat;
import org.ezcode.codetest.domain.game.model.character.GameCharacter;
import org.ezcode.codetest.domain.game.model.character.Inventory;
import org.ezcode.codetest.domain.game.model.encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.encounter.EncounterLog;
import org.ezcode.codetest.domain.game.model.encounter.RandomEncounterEffect;
import org.ezcode.codetest.domain.game.strategy.encounter.EncounterStrategy;
import org.springframework.stereotype.Component;

@Component
public class StatDecrease implements EncounterStrategy {

	@Override
	public RandomEncounterEffect getEffect() {
		return RandomEncounterEffect.STAT_DECREASE;
	}

	@Override
	public void eventHappen(GameCharacter character, Inventory inventory, CharacterContext context, EncounterLog log) {

		CharacterRealStat realStat = character.getRealStat();
		realStat.applySpeedChange(-1.0);

		log.add("어둡고 축축한 동굴에서 신비로운 푸른 영약을 발견했습니다.");
		log.add("%s(은)는 반짝이는 병에 담긴 영약을 망설임 없이 들이켰습니다.", context.getName());
		log.add("처음엔 온몸에 열기가 퍼지며 기분 좋은 감각이 스쳤지만, 곧바로 머리를 찌르는 듯한 강렬한 두통이 몰려옵니다.");
		log.add("근육이 풀리고 관절이 무거워지며, 발끝까지 퍼지는 무력감에 %s(은)는 비틀거리기 시작합니다.", context.getName());
		log.add("영약의 정체는 단순한 축복이 아닌 저주였습니다. 민첩성이 영구적으로 감소하였습니다. (스피드 -1)");

		log.setIsPositive(false);
	}
}
