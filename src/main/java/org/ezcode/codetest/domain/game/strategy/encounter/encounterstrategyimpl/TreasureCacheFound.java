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
public class TreasureCacheFound implements EncounterStrategy {

	private final ItemRepository itemRepository;

	@Override
	public RandomEncounterEffect getEffect() {
		return RandomEncounterEffect.TREASURE_CACHE_FOUND;
	}

	@Override
	public void eventHappen(
		GameCharacter character,
		Inventory inventory,
		CharacterContext context,
		EncounterLog log
	) {
		String player = context.getName();

		log.add("%s(은)는 잠시 망설이다 결국 왼쪽 상자를 열기로 결심합니다.", player);
		log.add("%s(은)는 심호흡을 한 뒤, ‘이번엔 좀 제대로 된 게 나오겠지?’ 하는 기대를 품고 상자의 뚜껑을 조심스레 열었습니다...", player);

		List<Item> defenceList = itemRepository.findAllByItemCategory(ItemCategory.DEFENCE);
		int randomIndex = ThreadLocalRandom.current().nextInt(defenceList.size());
		Item item = defenceList.get(randomIndex);

		String grade = item.getGrade().getGrade();
		String name = item.getName();

		if (inventory.getDefences().contains(item.getId())) {
			character.earnGold(200L);
			log.add("뚜껑을 열자마자, 익숙한 무기. 아마 지난 던전에서도 주웠던 그 녀석이 눈에 들어옵니다.");
			log.add("고대 상자가 당신에게 묻는 듯합니다. “복붙된 무기는 어때? 대신 골드 200개는 덤이야.”");
			log.add("%s(은)는 200 골드를 얻었습니다.", player);
		} else {
			inventory.addItem(item.getItemType(), item.getId());
			log.add("그러자 고풍스러운 장식이 새겨진 방어구가 상자 속에서 조심스레 모습을 드러냈습니다.");
			log.add("%s(은)는 손끝으로 촉감을 느끼며, ‘이건 좀 다르군’ 하는 든든한 안도감을 느꼈습니다.", player);
			log.add("%s(은)는 고대의 숨결이 깃든 장비 (%s : %s)를 손에 넣었습니다. 역시 인생은 한 방.", player, grade, name);
		}

		log.setIsPositive(true);
	}
}
