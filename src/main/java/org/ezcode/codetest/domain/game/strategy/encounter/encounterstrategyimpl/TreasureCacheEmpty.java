package org.ezcode.codetest.domain.game.strategy.encounter.encounterstrategyimpl;

import org.ezcode.codetest.domain.game.model.character.GameCharacter;
import org.ezcode.codetest.domain.game.model.character.Inventory;
import org.ezcode.codetest.domain.game.model.encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.encounter.EncounterLog;
import org.ezcode.codetest.domain.game.model.encounter.RandomEncounterEffect;
import org.ezcode.codetest.domain.game.strategy.encounter.EncounterStrategy;
import org.springframework.stereotype.Component;

@Component
public class TreasureCacheEmpty implements EncounterStrategy {

	@Override
	public RandomEncounterEffect getEffect() {
		return RandomEncounterEffect.TREASURE_CACHE_EMPTY;
	}

	@Override
	public void eventHappen(
		GameCharacter character,
		Inventory inventory,
		CharacterContext context,
		EncounterLog log
	) {
		String player = context.getName();

		log.add("황무지 탐험에 정답은 없죠. 어쨌든 %s(은)는 '딱 봐도 수상한' 두 상자 중에서 고민 끝에 오른쪽을 선택했습니다. 직감? 아니면 단순한 포기일까요?", player);
		log.add("경고문이 뻔히 있는데도, 어차피 내 인생 선택은 다 이런 식이지 뭐 하고 뚜껑을 열었습니다.");
		log.add("상자 안엔… 먼지, 거미줄, 그리고 어디선가 굴러온 대차게 박살난 희망 한 조각. 고맙다, 황무지.");
		log.add("순간, 왼쪽 상자도 한 번 열어볼까 잠깐 머릿속에 스쳐갔지만, 이쯤에서 사고 더 키우면 누가 손해겠습니까? 그럴 땐 미련 없이 포기하는 게 황무지 생존법.");
		log.add("%s(은)는 빈 상자를 한참 바라보다가, ‘와, 이 정도면 제작진이 내 운명 조롱하는 거지?’라는 생각이 들었습니다.", player);
		log.add("그래도 긍정적으로 생각합시다. 적어도 폭탄이 들어있진 않았네요.");
		log.add("어깨를 으쓱이며 ‘오늘도 경험치 하나 챙겼다’는 마음으로 자리를 뜹니다. 황무지에선 이런 날도 있는 법이니까요.");

		log.setIsPositive(false);
	}
}
