package org.ezcode.codetest.domain.game.strategy.encounter.encounterstrategyimpl;

import org.ezcode.codetest.domain.game.model.character.GameCharacter;
import org.ezcode.codetest.domain.game.model.character.Inventory;
import org.ezcode.codetest.domain.game.model.encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.encounter.EncounterLog;
import org.ezcode.codetest.domain.game.model.encounter.RandomEncounterEffect;
import org.ezcode.codetest.domain.game.strategy.encounter.EncounterStrategy;
import org.springframework.stereotype.Component;

@Component
public class GamblingBad implements EncounterStrategy {

	@Override
	public RandomEncounterEffect getEffect() {
		return RandomEncounterEffect.GAMBLING_BAD;
	}

	@Override
	public void eventHappen(GameCharacter character, Inventory inventory, CharacterContext context, EncounterLog log) {
		long lossGold = Math.min(200L, character.getGold());
		character.earnGold(-lossGold);

		log.add("깊은 숨을 내쉬며 모든 것을 걸고 주사위를 던졌지만, 냉혹한 현실은 외면했습니다.");
		log.add("탁자 위에서 굴러 떨어진 마지막 패는 차갑게 등을 돌렸고, 주변의 환호성은 어느새 조롱으로 바뀌었습니다.");
		log.add("%s(은)는 씁쓸한 표정으로 조용히 자리를 떴습니다. 손에 쥔 건 허무함뿐, %d 골드는 사라지고 없습니다.", context.getName(), lossGold);

		log.setIsPositive(false);
	}
}
