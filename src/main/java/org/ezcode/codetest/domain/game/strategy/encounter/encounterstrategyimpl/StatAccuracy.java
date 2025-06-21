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
public class StatAccuracy implements EncounterStrategy {

	@Override
	public RandomEncounterEffect getEffect() {
		return RandomEncounterEffect.STAT_ACCURACY;
	}

	@Override
	public void eventHappen(GameCharacter character, Inventory inventory, CharacterContext context, EncounterLog log) {

		CharacterRealStat realStat = character.getRealStat();
		String player = context.getName();

		int accuracyChange = ThreadLocalRandom.current().nextBoolean() ? 1 : -1;
		realStat.applyAccuracyChange(accuracyChange);

		log.add("%s(은)는 붉은 빛이 희미하게 새어나오는 출입구를 지나, 먼지와 녹슨 기계 소리가 가득한 연구소 구석으로 조심스레 들어섰습니다.", player);
		log.add("그곳에서 반짝이는 이상한 병 하나를 발견했습니다. 뚜껑을 열자, 오래된 기계에서 피어나는 듯한 신비로운 연기가 퍼졌죠.");

		if (accuracyChange > 0) {
			log.add("망설임 없이 한 모금 들이켰더니, 몸속에서 뜨거운 불꽃이 터지는 것 같은 기운이 솟구쳤습니다!");
			log.add("시야가 갑자기 맑아지고 전신에 전율이 퍼지며, ‘이제 싸울 준비가 됐다’는 자신감이 폭발했습니다.");
			log.add("명중이 영구적으로 상승했습니다! (명중 +1)");
			log.setIsPositive(true);
		} else {
			log.add("망설임 없이 한 모금 들이켰더니, 갑자기 몸이 무거워지고 힘이 빠지는 걸 느꼈습니다. 이거 마신 게 실수였나 봅니다.");
			log.add("명중이 영구적으로 감소했습니다. (명중 -1)");
			log.setIsPositive(false);
		}
	}
}
