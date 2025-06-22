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
		double successChance = Math.min(atk / 100.0, 1.0);
		boolean win = ThreadLocalRandom.current().nextDouble() < successChance;

		log.add("%s(은)는 도망치지 않았습니다. 어차피 인생도 얻어맞는 거니까요.", playerContext.getName());
		log.add("앞에 선 건 평범한 할머니들이 아니라, 지팡이와 롤링 핀을 든 생활 밀착형 파이터들입니다.");
		log.add("“손주 교육은 이렇게 하는 거란다”라는 말과 함께, 첫 회오리가 다가옵니다...");

		if (win) {
			character.earnGold(100L);
			realStat.applyCritChange(0.5);
			log.add("첫 지팡이를 피한 %s(은)는 반격을 날립니다. 할머니 한 분의 틀니가 공중으로 날아오릅니다!", playerContext.getName());
			log.add("순간의 침묵. 남은 도적단은 무언가 결심한 듯 수줍게 뒷걸음질칩니다. ‘다음엔 꿀 넣은 파이로 올게…’");
			log.add("쓰러진 할머니의 앞치마에서 100골드를 발견한 당신은, 그것이 생일용 비상금이었다는 걸 모른 채 기뻐합니다.");
			log.add("경험은 피로 쓰는 법. %s(은)는 싸움에서 감각을 벼려냈습니다. (치명타 확률 +0.5)", playerContext.getName());
			log.setIsPositive(true);
		} else {
			long lost = character.getGold() / 2;
			character.loseGold(lost);
			realStat.applyDefChange(-0.2);
			log.add("그러나 결심과는 무색하게 %s(은)는 첫 지팡이에 명치가 눌리고, 두 번째 롤링 핀에 시야가 돌아갑니다.", playerContext.getName());
			log.add("누군가는 ‘치료용 허브차’를 권했지만, 사실 그건 다시 맞으라는 신호였습니다.");
			log.add("정신을 차려보니 골드 %d이 사라지고, 무릎엔 ‘할머니파이맛’이라는 딱지가 붙어 있습니다.", lost);
			log.add("%s(은)는 이번 싸움이 육체보다 자존심에 더 큰 데미지를 줬음을 느끼며, 방어력이 0.2 감소했습니다. (방어력 -0.2)", playerContext.getName());
			log.setIsPositive(false);
		}
	}
}