package org.ezcode.codetest.domain.game.strategy.encounter.encounterstrategyimpl;

import org.ezcode.codetest.domain.game.model.character.GameCharacter;
import org.ezcode.codetest.domain.game.model.character.Inventory;
import org.ezcode.codetest.domain.game.model.encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.encounter.EncounterLog;
import org.ezcode.codetest.domain.game.model.encounter.RandomEncounterEffect;
import org.ezcode.codetest.domain.game.strategy.encounter.EncounterStrategy;
import org.springframework.stereotype.Component;

@Component
public class BossEncounterGood implements EncounterStrategy {

	@Override
	public RandomEncounterEffect getEffect() {
		return RandomEncounterEffect.BOSS_BATTLE_GOOD;
	}

	@Override
	public void eventHappen(
		GameCharacter character,
		Inventory playerInventory,
		CharacterContext playerContext,
		EncounterLog log
	) {
		long rewardGold = 350L;
		character.earnGold(rewardGold);

		log.add("숨조차 가라앉을 만큼 고요한 유적의 가장 깊숙한 곳, 발밑에서 진동처럼 퍼지는 낮은 숨결 소리가 공간을 가릅니다.");
		log.add("눈앞을 가득 채운 것은 어마어마한 존재감의 고대 드래곤. 그 비늘은 달빛을 받아 찬란하게 빛났고, 눈동자는 마치 별처럼 깊었습니다.");
		log.add(playerContext.getName() + "(은)는 두려움을 억누른 채 무릎을 꿇었고, 드래곤은 긴 침묵 끝에 입을 열었습니다.");
		log.add("\"용기 있는 자여, 수많은 시련을 견딘 그대의 발걸음에 존경을 표하노라.\"");
		log.add("굵고 울림 있는 목소리와 함께, 드래곤은 앞발로 황금과 보석이 가득 담긴 고대의 보물상자를 당신 앞으로 밀어냈습니다.");
		log.add("그 안에서 " + rewardGold + "골드가 빛을 내며 모습을 드러냅니다. 전설적 존재에게 인정받은 기쁨이 가슴 깊이 파고듭니다.");
		log.setIsPositive(true);
	}
}
