package org.ezcode.codetest.domain.game.strategy.encounter.encounterstrategyimpl;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.ezcode.codetest.domain.game.model.character.GameCharacter;
import org.ezcode.codetest.domain.game.model.character.Inventory;
import org.ezcode.codetest.domain.game.model.encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.encounter.EncounterLog;
import org.ezcode.codetest.domain.game.model.encounter.RandomEncounterEffect;
import org.ezcode.codetest.domain.game.model.item.Item;
import org.ezcode.codetest.domain.game.model.item.ItemCategory;
import org.ezcode.codetest.domain.game.repository.ItemRepository;
import org.ezcode.codetest.domain.game.strategy.encounter.EncounterStrategy;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AncientRuinsTreasure implements EncounterStrategy {

	private final ItemRepository itemRepository;

	@Override
	public RandomEncounterEffect getEffect() {
		return RandomEncounterEffect.ANCIENT_RUINS_TREASURE;
	}

	@Override
	public void eventHappen(
		GameCharacter character,
		Inventory inventory,
		CharacterContext context,
		EncounterLog log
	) {
		String player = context.getName();

		log.add("당신은 밝은 회랑을 선택했습니다. 덜 위협적으로 보인다고요? 그럼 함정은 덜 있겠죠.");
		log.add("쌓여 있던 잿빛 돌더미 사이로 희미한 빛줄기가 퍼지며, 벽면의 낙서 같은 벽화들이 은은히 드러납니다.");
		log.add("%s(은)는 신중하게 발걸음을 옮기며, 고요한 공기를 가르고 미지의 방에 들어섭니다.", player);

		log.add("중앙에는 도저히 던전에서 볼 수 없는 고급스러운 상자가 놓여 있습니다. 마치 '플레이어 보상용'이라고 써 있는 것처럼요.");

		List<Item> weaponList = itemRepository.findAllByItemCategory(ItemCategory.WEAPON);
		int randomIndex = ThreadLocalRandom.current().nextInt(weaponList.size());
		Item item = weaponList.get(randomIndex);

		String grade = item.getGrade().getGrade();
		String weaponName = item.getName();

		if (inventory.getWeapons().contains(item.getId())) {
			character.earnGold(200L);
			log.add("뚜껑을 열자마자, 익숙한 무기. 아마 지난 던전에서도 주웠던 그 녀석입니다.");
			log.add("고대 상자가 당신에게 묻는 듯합니다. “복붙된 무기는 어때? 대신 골드 200개는 덤이야.”");
		} else {
			inventory.addItem(item.getItemType(), item.getId());
			log.add("%s(은)는 숨을 들이쉬고 조심스럽게 상자를 엽니다... (%s: %s) 이 은은한 광채를 뿜으며 등장합니다.",player, grade, weaponName);
			log.add("그 순간, 배경 음악이 깔리고 화면 중앙에 '획득!'이 떠오를 것만 같습니다.");
			log.add("누군가는 이 무기를 고대의 유산이라 부르겠지만, 당신은 그냥 '오늘의 운빨'이라 부릅니다.");
		}

		log.setIsPositive(true);
	}
}
