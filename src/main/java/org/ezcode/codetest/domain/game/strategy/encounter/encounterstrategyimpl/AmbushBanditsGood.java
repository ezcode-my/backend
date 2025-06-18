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
public class AmbushBanditsGood implements EncounterStrategy {

	@Override
	public RandomEncounterEffect getEffect() {
		return RandomEncounterEffect.AMBUSH_BANDITS_FIGHT;
	}

	@Override
	public void eventHappen(
		GameCharacter character,
		Inventory playerInventory,
		CharacterContext playerContext,
		EncounterLog log
	) {
		CharacterRealStat realStat = character.getRealStat();
		double atk = playerContext.getAtk();
		double successChance = atk / 100.0;

		log.add("낮게 으스스하게 울려 퍼지는 발소리, %s(은)는 숨결을 고르며 칼자루를 움켜쥡니다.", playerContext.getName());
		log.add("검은 망토를 두른 무리의 그림자가 성큼성큼 다가오며, 긴장감이 온몸을 지배합니다.");

		boolean win = ThreadLocalRandom.current().nextDouble() < successChance;
		if (win) {
			character.earnGold(100L);
			realStat.applyCritChange(0.5);
			log.add("%s(은)는 날카로운 일격으로 도적의 허점을 정확히 찌릅니다!", playerContext.getName());
			log.add("도적이 비틀거리며 쓰러진 순간, %s(은)는 빠르게 주머니를 뒤져 100골드를 확보합니다.", playerContext.getName());
			log.add("숨결이 거칠게 오가지만, 승리의 여운이 온 몸에 전해집니다.");
			log.add("%s(은)는 전투 경험으로 예리해진 감각을 실감합니다. (치명타 확률 +0.5)", playerContext.getName());
			log.setIsPositive(true);
		} else {
			long lost = character.getGold() / 2;
			character.earnGold(-lost);
			realStat.applyDefChange(-1.0);
			log.add("%s(은)는 필사적으로 반격을 시도했으나, 강렬한 일격에 무너집니다...", playerContext.getName());
			log.add("차가운 땅바닥에 쓰러진 채, 도적들의 비웃음이 잔혹한 울림으로 귀에 맴돕니다.");
			log.add("가진 골드의 절반인 %d가 도적들의 주머니로 사라지고, %s(은)는 고통 속에서 몸을 일으킵니다.", lost, playerContext.getName());
			log.add("%s(은)는 전투 후유증으로 방어력이 영구히 1만큼 약해진 것을 느끼며, 아릿한 상처를 곱씹습니다.", playerContext.getName());
			log.setIsPositive(false);
		}
	}
}