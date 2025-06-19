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
		return RandomEncounterEffect.BOSS_BATTLE_BAD;
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

		log.add("어둠 속에서 천천히 눈을 뜬 고대의 골렘이 몸을 일으키며 거대한 그림자를 드리웁니다.");
		log.add("골렘의 육중한 주먹이 바람을 가르며 " + player + "에게 맹렬히 날아듭니다! %,.1f의 피해를 입었습니다...!", rawBossAtk);

		if (!alive) {
			realStat.applyDefChange(-1.0);
			log.add("막아보려 애썼으나, 충격은 상상을 초월했습니다. " + player + "(은)는 골렘의 엄청난 힘에 무너지고 맙니다.");
			log.add("공격을 무리하게 방어하던 중 " + player + "의 팔에 날카로운 고통이 몰려오고, 뼈가 부러지는 끔찍한 소리가 울려 퍼집니다. (방어력 -1)");
			log.add(player + "(은)는 고통을 참으며 부러진 팔을 붙잡고 간신히 골렘으로부터 멀어집니다.");
			log.setIsPositive(false);
		} else {
			log.add(player + "(은)는 모든 힘을 끌어모아 간신히 그 무시무시한 일격을 버텨냈습니다.");
			log.add("충격에 잠시 정신을 잃을 뻔했지만, " + player + "(은)는 이를 악물고 흔들리는 몸을 가까스로 다잡습니다.");
			log.add("상처 입은 몸으로 겨우 거리를 벌려, 골렘의 무거운 발걸음이 멀어질 때까지 필사적으로 달아났습니다.");
			log.setIsPositive(true);
		}
	}
}
