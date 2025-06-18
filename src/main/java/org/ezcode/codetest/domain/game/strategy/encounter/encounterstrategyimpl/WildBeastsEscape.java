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
public class WildBeastsEscape implements EncounterStrategy {

	@Override
	public RandomEncounterEffect getEffect() {
		return RandomEncounterEffect.WILD_BEASTS_ESCAPE;
	}

	@Override
	public void eventHappen(
		GameCharacter character,
		Inventory inventory,
		CharacterContext context,
		EncounterLog log
	) {
		CharacterRealStat real = character.getRealStat();
		double speed    = context.getSpeed();
		double evasion  = context.getEvasion();
		double chance   = (speed + evasion) / 100.0;

		log.add("울창한 숲 속, 짙은 안개 너머로 굶주린 맹수의 으르렁거림이 귓가를 울립니다.");
		log.add(context.getName() + "(은)는 재빠르게 주위를 살피고, 낙엽을 밟지 않도록 조심스럽게 몸을 낮춥니다.");
		log.add("숨죽인 채 날카로운 본능에 몸을 맡기고 도망을 시도합니다...");

		boolean escaped = ThreadLocalRandom.current().nextDouble() < chance;

		if (escaped) {
			real.applySpeedChange(0.5);
			log.add("나뭇가지 사이를 민첩하게 가르며, 마치 숲의 일부처럼 자연스럽게 움직입니다.");
			log.add(context.getName() + "(은)는 위기 속에서도 집중력을 잃지 않고, 야생의 위협을 완벽하게 따돌렸습니다! (스피드 +0.5)");
			log.setIsPositive(true);
		} else {
			real.applyCritChange(-0.5);
			log.add("뒤에서 덮쳐오는 기척에 놀라 몸을 틀지만, 맹수의 발톱이 팔을 깊게 긁고 지나갑니다.");
			log.add("격통에 휘청이는 사이, " + context.getName() + "(은)는 가까스로 몸을 일으켜 도망치지만 팔의 부상은 크고 깊습니다.");
			log.add("팔을 심하게 다쳐 예리한 일격을 날릴 힘과 정밀함을 잃었습니다. (치명타 확률 -0.5)");
			log.add("숨을 헐떡이며 숲을 빠져나온 " + context.getName() + "(은)는, 무력감과 함께 상처를 감싸쥐고 한동안 움직이지 못합니다.");
			log.setIsPositive(false);
		}
	}
}
