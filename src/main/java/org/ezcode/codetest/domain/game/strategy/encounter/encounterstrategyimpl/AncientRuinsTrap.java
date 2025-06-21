package org.ezcode.codetest.domain.game.strategy.encounter.encounterstrategyimpl;

import org.ezcode.codetest.domain.game.model.character.GameCharacter;
import org.ezcode.codetest.domain.game.model.character.Inventory;
import org.ezcode.codetest.domain.game.model.encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.encounter.EncounterLog;
import org.ezcode.codetest.domain.game.model.encounter.RandomEncounterEffect;
import org.ezcode.codetest.domain.game.strategy.encounter.EncounterStrategy;
import org.springframework.stereotype.Component;

@Component
public class AncientRuinsTrap implements EncounterStrategy {

	@Override
	public RandomEncounterEffect getEffect() {
		return RandomEncounterEffect.ANCIENT_RUINS_TRAP;
	}

	@Override
	public void eventHappen(
		GameCharacter character,
		Inventory inventory,
		CharacterContext context,
		EncounterLog log
	) {
		String player = context.getName();

		log.add("“탐험가의 본능 +1”… 그런 건 없습니다. 당신은 어두운 통로 쪽이 더 ‘진짜’ 같다고 판단했습니다.");
		log.add("하지만 첫걸음을 내딛는 순간, 발밑에서 ‘딸깍’ 하는 음산한 소리와 함께 진동이 퍼집니다.");

		log.add("%s(은)는 반사적으로 몸을 틀었지만, 발밑의 바닥은 이미 어디론가 휙 사라지고 있었습니다.", player);
		log.add("간신히 벽 틈에 매달리긴 했지만... 허리춤의 금화 주머니는 ‘희생정신’을 발휘해 먼저 탈출했습니다.");

		long lostGold = Math.min(100L, character.getGold());
		character.loseGold(lostGold);

		log.add("아래에선 ‘짤랑짤랑’ 소리가 메아리치고, 당신의 %d골드는 이제 유물과 함께 박제될 예정입니다.".formatted(lostGold));
		log.add("%s(은)는 가까스로 올라왔지만, 어쩐지 어깨는 무겁고 주머니는 가볍습니다.", player);

		log.setIsPositive(false);
	}
}
