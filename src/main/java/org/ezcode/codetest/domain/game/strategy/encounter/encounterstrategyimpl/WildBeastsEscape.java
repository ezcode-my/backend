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
		String player = context.getName();

		double speed   = context.getSpeed();
		double evasion = context.getEvasion();
		double chance = Math.min((speed + evasion) / 100.0, 1.0);

		log.add("%s(은)는 망설임 끝에, 싸움 대신 도망을 택합니다. 이 선택이 비겁한지 아닌지는 살아남은 다음에나 따져보죠.", player);
		log.add("%s(은)는 본능적으로 숨을 죽이고, 낙엽 하나 밟지 않겠다는 각오로 몸을 낮춥니다.", player);
		log.add("이런 상황에서 싸우는 건 용기가 아니라 어리석음이죠. 지금 필요한 건 뚜렷한 목적과 빠른 다리뿐입니다.");
		log.add("도망—그 위대한 선택을 하며, %s(은)는 숲의 그림자 속으로 뛰어듭니다!", player);

		boolean escaped = ThreadLocalRandom.current().nextDouble() < chance;

		if (escaped) {
			real.applySpeedChange(0.5);
			log.add("다행히도 %s의 다리는 ‘목숨은 소중하다’는 교훈을 몸소 실천했습니다. 이쯤 되면 도망도 특기입니다.", player);
			log.add("돌뿌리를 딛고 튕기듯 나아가며, 가지 사이를 슬라럼 타듯 빠져나갑니다.");
			log.add("맹수의 포효는 멀어지고, %s(은)는 자신이 살아 있다는 것에 잠시 벅찬 감정을 느낍니다.", player);
			log.add("하지만 감동은 금물입니다. 이건 생존이 아니라 단지 다음 참사를 위한 연장일 뿐이니까요. (스피드 +0.5)");
			log.setIsPositive(true);
		} else {
			real.applyCritChange(-0.5);
			log.add("하지만 생각과는 다르게 숨소리마저 삼키며 몸을 숨기던 그 순간, —뒤에서 들려온 건 ‘철컥’, 그리고 운명의 통지서.");
			log.add("맹수의 발톱이 %s의 팔을 찢고 지나갑니다. 정확히 말하면, 이력서에 새로운 흉터 항목을 추가했죠.", player);
			log.add("비틀거리며 도망친 %s(은)는 깨달았습니다. 자연은 언제나 공정한데, 당신만 불합격입니다.", player);
			log.add("치명타 감각을 잃었습니다. 그러니까 다음부턴 도망 말고, 보험을 드세요. (치명타 확률 -0.5)");
			log.setIsPositive(false);
		}
	}
}
