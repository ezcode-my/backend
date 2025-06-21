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

		String player = context.getName();
		character.earnGold(500);
		log.add("%s(은)는 6을 골랐습니다. 곧 이어서 주사위가 굴러가며 속으로 기도합니다. 평소엔 무신론자였지만, 지금만큼은 다 믿습니다.", player);
		log.add("주사위는 테이블 위를 구르다 굼뜬 마무리를 짓습니다. 모두의 시선이 한곳에 꽂힙니다.");
		log.add("그리고 그 순간, 기적이 일어났습니다. -6- 주사위의 눈은 정확히 6 이었습니다. 혹은 시스템 오류일 수도 있겠네요. 그래도 돈이 나오면 진실은 중요하지 않습니다.");
		log.add("딜러는 마지못한 표정으로 박수를 치고, 누군가는 '부정이야!'라고 외쳤지만 곧 조용해집니다. 승자는 항상 정의니까요.");
		log.add("“축하합니다, 손님.” 기계처럼 반복된 멘트지만, 쏟아지는 칩 소리는 언제 들어도 감동적입니다. +500골드. 그리고 미묘한 우월감.");
		log.add("%s(은)는 천천히 일어나며 생각합니다. ‘이걸로 인생 역전은 무리더라도, 오늘 저녁은 고기겠군.’", player);
		log.add("물론, 경비원의 시선이 점점 날카로워지는 건 착각이 아닙니다. 세상에 공짜는 없으니까요.");

		log.setIsPositive(true);
	}
}