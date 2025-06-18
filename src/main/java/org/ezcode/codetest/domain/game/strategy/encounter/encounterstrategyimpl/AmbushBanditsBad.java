package org.ezcode.codetest.domain.game.strategy.encounter.encounterstrategyimpl;

import java.util.concurrent.ThreadLocalRandom;

import org.ezcode.codetest.domain.game.model.character.CharacterRealStat;
import org.ezcode.codetest.domain.game.model.character.GameCharacter;
import org.ezcode.codetest.domain.game.model.character.Inventory;
import org.ezcode.codetest.domain.game.model.encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.encounter.EncounterLog;
import org.ezcode.codetest.domain.game.model.encounter.RandomEncounterEffect;
import org.ezcode.codetest.domain.game.strategy.encounter.EncounterStrategy;
import org.springframework.stereotype.Component;

@Component
public class AmbushBanditsBad implements EncounterStrategy {

	@Override
	public RandomEncounterEffect getEffect() {
		return RandomEncounterEffect.AMBUSH_BANDITS_ESCAPE;
	}

	@Override
	public void eventHappen(
		GameCharacter character,
		Inventory playerInventory,
		CharacterContext playerContext,
		EncounterLog log
	) {
		CharacterRealStat realStat = character.getRealStat();
		double speed = playerContext.getSpeed();
		double evasion = playerContext.getEvasion();
		double successChance = Math.min((speed + evasion) / 100.0, 1.0);

		log.add("바람에 실려온 먼지 냄새 속에서, %s의 귓가에 낮은 속삭임이 전해집니다.", playerContext.getName());
		log.add("그 순간, 어둠 속에 숨어있던 도적들이 일제히 모습을 드러냅니다!");

		log.add("%s(은)는 심호흡을 짧게 들이쉬며, 순간적으로 몸의 균형을 재조정합니다.", playerContext.getName());

		boolean escaped = ThreadLocalRandom.current().nextDouble() < successChance;

		if (escaped) {
			realStat.applySpeedChange(0.5);
			log.add("번득이는 반사신경으로 %s(은)는 번개처럼 움직이며 도적들의 시선을 교묘히 피합니다.", playerContext.getName());
			log.add("바위 틈이나 나무 뒤로 몸을 숨기며, 숨결이 차오르는 가운데도 심장이 한 박자 느려진 듯 안정됩니다.");
			log.add("%s(은)는 무사히 도망치면서 자신 안에 감춰진 민첩함을 느낍니다. (스피드 +0.5)", playerContext.getName());
			log.setIsPositive(true);
		} else {
			long lostGold = character.getGold() / 2;
			character.earnGold(-lostGold);
			realStat.applyCritChange(-0.5);
			log.add("%s(은)는 한순간 주춤하며 방향을 틀지만, 도적들의 촘촘한 그물이 덮치듯 던져집니다.", playerContext.getName());
			log.add("숨이 막히는 고통 속에서, 차갑고 날카로운 채찍 같은 타격이 %s의 몸을 스칩니다.", playerContext.getName());
			log.add("%s(은)는 고통을 간신히 견디며, 치명타에 대한 예리함이 무뎌지는 것을 느낍니다. (치명타 -0.5)", playerContext.getName());
			log.add("끝내 도적들은 눈앞에서 %s의 소중한 골드 %d를 챙겨 달아나고, 허탈감이 %s의 폐부에 내려앉습니다.", playerContext.getName(), lostGold, playerContext.getName());
			log.setIsPositive(false);
		}
	}
}