package org.ezcode.codetest.domain.game.strategy.encounter.encounterstrategyimpl;

import java.util.List;
import java.util.Random;

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

		log.add("고즈넉한 숲속 폐허, 햇살에 반사된 반짝임을 따라가자 무성한 덩굴 속에서 낡은 보물 상자가 모습을 드러냅니다.");
		log.add(player + "(은)는 심호흡을 하고, 두근거리는 마음으로 상자의 뚜껑을 조심스레 엽니다...");

		List<Item> defenceList = itemRepository.findAllByItemCategory(ItemCategory.DEFENCE);
		Random random = new Random();
		int index = random.nextInt(defenceList.size());
		Item item = defenceList.get(index);

		String grade = item.getGrade().getGrade();
		String name = item.getName();

		if (inventory.getDefences().contains(item.getId())) {
			character.earnGold(200L);
			log.add("그러나 그 안에 있던 방어구는 이미 익숙한 것이었습니다.");
			log.add("대신 상자 밑바닥에서 발견된 황금 주화가 햇빛에 반짝이며 %s(을)를 위로합니다. (+200골드)", player);
			log.add(player + "(은)는 아쉬움을 뒤로하고, 반짝이는 금화를 조용히 챙겼습니다.");
		} else {
			inventory.addItem(item.getItemType(), item.getId());
			log.add("고풍스러운 장식이 새겨진 방어구가 조심스레 모습을 드러냅니다.");
			log.add(player + "(은)는 손끝으로 그 촉감을 느끼며, 든든한 안도감에 미소를 지었습니다.");
			log.add(player + "(은)는 고대의 숨결이 깃든 장비 (" + grade + " : " + name + ")를 손에 넣었습니다.");
		}

		log.setIsPositive(true);
	}
}
