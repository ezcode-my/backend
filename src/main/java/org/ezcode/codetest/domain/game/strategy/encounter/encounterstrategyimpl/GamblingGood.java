package org.ezcode.codetest.domain.game.strategy.encounter.encounterstrategyimpl;

import org.ezcode.codetest.domain.game.model.character.GameCharacter;
import org.ezcode.codetest.domain.game.model.character.Inventory;
import org.ezcode.codetest.domain.game.model.encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.encounter.EncounterLog;
import org.ezcode.codetest.domain.game.model.encounter.RandomEncounterEffect;
import org.ezcode.codetest.domain.game.strategy.encounter.EncounterStrategy;
import org.springframework.stereotype.Component;

@Component
public class GamblingGood implements EncounterStrategy {

	@Override
	public RandomEncounterEffect getEffect() {
		return RandomEncounterEffect.GAMBLING_GOOD;
	}

	@Override
	public void eventHappen(GameCharacter character, Inventory inventory, CharacterContext context, EncounterLog log) {
		character.earnGold(200L);

		log.add("반짝이는 조명 아래, %s(은)는 마지막 패를 조심스레 펼쳤습니다. 순간, 정적을 가르며 주변이 술렁이기 시작합니다.", context.getName());
		log.add("딜러의 손이 멈추고, 긴장한 침묵 속에서 테이블 위가 환호성으로 뒤덮입니다!");
		log.add("행운의 여신은 미소 지었고, 탑 위에 쌓인 칩 더미가 %s의 앞으로 쏟아졌습니다. (+200골드)", context.getName());
		log.add("승부의 짜릿함과 함께 느껴지는 승자의 여유가 도박장을 가득 채웁니다.");

		log.setIsPositive(true);
	}
}