package org.ezcode.codetest.domain.game.strategy.encounter.encounterstrategyimpl;

import org.ezcode.codetest.domain.game.model.character.GameCharacter;
import org.ezcode.codetest.domain.game.model.character.Inventory;
import org.ezcode.codetest.domain.game.model.encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.encounter.EncounterLog;
import org.ezcode.codetest.domain.game.model.encounter.RandomEncounterEffect;
import org.ezcode.codetest.domain.game.strategy.encounter.EncounterStrategy;
import org.springframework.stereotype.Component;

@Component
public class GamblingBad implements EncounterStrategy {

	@Override
	public RandomEncounterEffect getEffect() {
		return RandomEncounterEffect.GAMBLING_BAD;
	}

	@Override
	public void eventHappen(GameCharacter character, Inventory inventory, CharacterContext context, EncounterLog log) {

		String player = context.getName();
		long lossGold = Math.min(200L, character.getGold());
		character.earnGold(-lossGold);

		log.add("%s(은)는 3을 골랐습니다. 주사위는 그 선택을 무시하며, 딴 곳에서 한가롭게 굴러갑니다.", player);
		log.add("‘3’은커녕 주사위는 ‘3’ 근처에도 가지 않았습니다. 마치 자기 일 아닌 듯 행동하네요.");
		log.add("%d골드가 사라졌습니다. 돈도, 체면도, 남은 건 허탈한 웃음뿐입니다.", lossGold);
		log.add("%s(은)는 자리에서 일어나며 중얼거렸습니다. ‘인생 역전? 오늘도 그냥 방송국 뉴스나 보며 시간을 때우자고.’", player);
		log.add("도박장은 다시 조용해졌고, 누군가는 벌써 다음 바보를 기다리고 있었습니다. 아마 그 바보는 바로 당신일 겁니다.");

		log.setIsPositive(false);
	}
}
