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
public class BossEncounterBad implements EncounterStrategy {

	@Override
	public RandomEncounterEffect getEffect() {
		return RandomEncounterEffect.BOSS_BATTLE;
	}

	@Override
	public void eventHappen(
		GameCharacter character,
		Inventory playerInventory,
		CharacterContext playerContext,
		EncounterLog log
	) {
		CharacterRealStat realStat = character.getRealStat();
		double playerDef = realStat.getDef();

		int variance = ThreadLocalRandom.current().nextInt(-10, 11);
		double rawBossAtk = 50 + variance;
		double bossAtk = playerDef + rawBossAtk;

		boolean alive = playerContext.playerDamaged(bossAtk);

		String player = playerContext.getName();

		log.add("당신은 한숨을 내쉬며, 문 쪽으로 도망치려던 발걸음을 거둡니다.");
		log.add("“그래, 한 대 맞고 죽을 운명이면 그것도 나쁘지 않지.” 라는 생각은 대체 왜 드는 걸까요?");
		log.add("골렘이 기지개를 펴듯 팔을 들고, %s(을)를 향해 그대로 내려찍습니다! 피해: %,.1f", player, rawBossAtk);
		if (!alive) {
			realStat.applyDefChange(-1.0);
			log.add("방어 자세? 그런 건 애초에 없었습니다. %s(은)는 벽돌처럼 튕겨 나갑니다.", player);
			log.add("팔에서 ‘뚝’ 소리가 나고, 이어지는 고통은 '난 오늘 여기까지인가...'라는 느낌을 줍니다. (방어력 -1)");
			log.add("골렘은 흥미를 잃은 듯 다시 웅크리며 잠에 듭니다. 당신은 그 틈을 타 슬금슬금 기어 나옵니다.");
			log.setIsPositive(false);
		} else {
			log.add("놀랍게도, %s(은)는 살아 있습니다. 아니, 어쩌면 땅이 푹신했던 걸까요?", player);
			log.add("골렘은 당신을 ‘딱히 해치울 필요는 없는 존재’로 판단했는지, 이내 고개를 돌립니다.");
			log.add("고통은 심하지만, 적어도 몸은 붙어 있습니다. 감사합니다, 돌덩이 선생.");
			log.setIsPositive(true);
		}
	}
}
