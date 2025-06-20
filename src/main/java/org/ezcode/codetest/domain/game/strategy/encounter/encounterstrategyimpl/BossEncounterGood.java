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
		return RandomEncounterEffect.BOSS_ESCAPE;
	}

	@Override
	public void eventHappen(
		GameCharacter character,
		Inventory playerInventory,
		CharacterContext playerContext,
		EncounterLog log
	) {
		long rewardGold = 1000L;
		character.earnGold(rewardGold);

		log.add("당신은 고민 따위는 집에 두고, 본능적으로 금빛 문 안으로 몸을 던졌습니다.");
		log.add("뒤에서는 골렘이 천천히 고개를 돌리지만, 다행히 이쪽까지는 따라오지 않습니다. 일단은요.");
		log.add("문 안쪽에는 거대한 방, 그리고 그 중심에—믿기 어렵지만, 고대 드래곤이 있습니다.");
		log.add("뭐? 드래곤이요? 맞습니다. 당신은 보스전에서 도망쳤다가 더 큰 보스의 방에 들어왔습니다.");
		log.add(playerContext.getName() + "(은)는 반사적으로 무릎을 꿇습니다. 전투? 말도 안 됩니다. 눈빛 하나에 간장이 얼었거든요.");
		log.add("드래곤은 흥미롭다는 듯 바라보다, 웅장한 목소리로 말합니다. “비겁함도 용기의 한 형태지.”");
		log.add("그렇게 황금 상자 하나가 눈앞에 ‘떵’ 하고 떨어집니다. 그냥 받아요. 말 대답은 안 하는 게 좋아 보입니다.");
		log.add(rewardGold + "골드 획득! 의외로 이쪽이 덜 아프네요.");
		log.setIsPositive(true);
	}
}