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
public class WildBeastsAttack implements EncounterStrategy {

	@Override
	public RandomEncounterEffect getEffect() {
		return RandomEncounterEffect.WILD_BEASTS_ATTACK;
	}

	@Override
	public void eventHappen(
		GameCharacter character,
		Inventory inventory,
		CharacterContext context,
		EncounterLog log
	) {
		CharacterRealStat real = character.getRealStat();
		double atk   = context.getAtk();
		double chance = Math.min(atk / 100.0, 1.0);

		log.add("어둠을 가르며 거대한 포효가 울려퍼집니다. 무성한 수풀을 헤치고 날카로운 송곳니를 드러낸 맹수가 모습을 드러냅니다!");
		log.add(context.getName() + "(은)는 한 치의 망설임도 없이 무기를 쥐고, 맹수와 한 판 승부에 돌입합니다!");

		boolean victory = ThreadLocalRandom.current().nextDouble() < chance;

		if (victory) {
			real.applyDefChange(0.5);
			log.add("맹수의 덩치에 주눅들지 않고, " + context.getName() + "(은)는 정확한 일격으로 적의 급소를 찔러 쓰러뜨렸습니다!");
			log.add("전투의 긴장 속에서 몸은 더욱 단련되었고, 방어의 자세가 한층 깊어졌습니다. (방어력 +0.5)");
			log.setIsPositive(true);
		} else {
			real.applySpeedChange(-0.5);
			log.add("맹수의 매서운 발톱이 허공을 가르며 파고듭니다. " + context.getName() + "(은)는 피하지 못하고 몸을 강하게 얻어맞습니다.");
			log.add("의식을 간신히 붙잡은 채 숲을 헤매어 빠져나왔지만, 부상의 여파로 몸놀림이 둔해졌습니다. (스피드 -0.5)");
			log.setIsPositive(false);
		}
	}
}
