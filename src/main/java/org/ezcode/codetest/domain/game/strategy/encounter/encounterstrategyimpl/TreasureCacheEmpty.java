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

		log.add("고즈넉한 숲속 폐허, 햇살에 반사된 반짝임을 따라가자 무성한 덩굴 속에서 낡은 보물 상자가 모습을 드러냅니다.");
		log.add(player + "(은)는 모래와 먼지로 덮인 낡은 철제 상자를 힘겹게 열었습니다. 녹슨 경첩이 삐걱이며 오래된 비밀을 드러낼 듯 열리지만...");
		log.add("그 안에는 아무것도 없었습니다. 희미하게 흩날리는 먼지 외에는.");
		log.add(player + "(은)는 잔뜩 부푼 기대감이 무너지는 순간, 상자 안을 멍하니 바라보다가 조용히 한숨을 내쉽니다.");
		log.add("허탈감이 몰려오고, 마음 한 켠이 텅 비어버린 듯한 상실감에 한동안 발걸음을 떼지 못합니다.");
		log.add("결국, %s(은)는 어깨를 축 늘어뜨린 채 무거운 발걸음으로 터덜터덜 돌아섭니다.", player);

		log.setIsPositive(false);
	}
}
