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

		boolean escaped = ThreadLocalRandom.current().nextDouble() < successChance;

		if (escaped) {
			log.add("당신은 도망을 선택했습니다.");
			realStat.applySpeedChange(0.5);
			log.add("할머니들의 허리 통증이 약간 늦춰준 틈을 타, 간신히 빠져나갑니다.");
			log.add("휘두르던 지팡이가 헛돌며 회오리처럼 공중을 가르고, %s(은)는 그 사이로 미끄러지듯 빠져나옵니다.", playerContext.getName());
			log.add("등 뒤로 들려오는 “요즘 젊은 것들은 빠르기만 해선 안 돼!”라는 잔소리는 덤입니다.");
			log.add("도망엔 성공했지만, 마음 한켠엔 죄책감보다 더한 기묘한 존경심이 남습니다. (스피드 +0.5)");
			log.setIsPositive(true);
		} else {
			log.add("당신은 도망을 택했지만... 도망치지 못했습니다.");
			long lostGold = character.getGold() / 2;
			character.earnGold(-lostGold);
			realStat.applyCritChange(-0.5);
			log.add("첫 번째 지팡이 타격에 정신이 멍해집니다. 뒤이어 날아든 팬케이크 뒤집개가 머리를 스칩니다.");
			log.add("‘손주 재우기’라는 말 대신 ‘골드 재우기’가 우선이었던 모양입니다.");
			log.add("%s(은)는 고통 속에서 치명타 예리함이 무뎌짐을 느낍니다. (치명타 -0.5)", playerContext.getName());
			log.add("결국 할머니 도적단은 %s의 골드 %d를 훔쳐 달아났고, 당신에게는 차가운 파이 접시만 남겨졌습니다.",
				playerContext.getName(), lostGold);
			log.setIsPositive(false);
		}
	}
}