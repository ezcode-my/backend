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

		log.add("오래된 유적의 복도를 조심스레 걸어가던 순간, 돌벽에 새겨진 고대 문자가 희미하게 빛납니다.");
		log.add("갑자기 발밑의 돌바닥에서 묵직한 진동이 느껴지며, 먼지가 사방으로 퍼집니다!");

		log.add(player + "(은)는 본능적으로 몸을 뒤틀며 피하려 했지만, 이미 발밑의 바닥은 깊은 어둠 속으로 사라져버립니다.");
		log.add("빠르게 손을 뻗어 가까스로 벽의 틈새를 붙잡았으나, 허리춤에 달려 있던 금화 주머니가 풀려 어둠 속으로 떨어져 사라집니다...");

		long lostGold = Math.min(100L, character.getGold());
		character.earnGold(-lostGold);

		log.add("깊은 구덩이 아래에서 희미한 금속 소리가 울려 퍼지고, 당신의 소중한 금화 " + lostGold + "개가 유적의 어둠 속으로 영원히 사라졌습니다.");
		log.add(player + "(은)는 팔에 힘을 주어 겨우 올라왔지만, 마음속에 남은 충격과 손실감은 쉽게 가시지 않을 것입니다.");

		log.setIsPositive(false);
	}
}
