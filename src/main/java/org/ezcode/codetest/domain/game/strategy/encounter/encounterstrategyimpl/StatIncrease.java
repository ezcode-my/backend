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
public class StatIncrease implements EncounterStrategy {

	@Override
	public RandomEncounterEffect getEffect() {
		return RandomEncounterEffect.STAT_INCREASE;
	}

	@Override
	public void eventHappen(GameCharacter character, Inventory inventory, CharacterContext context, EncounterLog log) {

		CharacterRealStat realStat = character.getRealStat();
		realStat.applyAtkChange(1.0);

		log.add("%s(은)는 축축하고 이끼 낀 동굴 깊숙한 곳에서 푸른빛이 스며 나오는 유리병을 발견합니다.", context.getName());
		log.add("병을 열자 희미한 증기가 피어오르고, 이국적인 향기가 주변을 감쌉니다.");
		log.add("망설임 없이 한 모금 삼키자, 속에서부터 불꽃처럼 뜨거운 기운이 솟구칩니다!");
		log.add("근육이 팽창하며, 전신에 퍼지는 전율과 함께 전투에 대한 자신감이 북받칩니다.");
		log.add("공격력이 영구적으로 상승했습니다! (공격력 +1)");

		log.setIsPositive(true);
	}
}