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

		log.add("고대 유적의 어두운 복도를 따라 걷던 중, 햇살이 갈라진 틈새 사이로 찬란하게 빛나며 오래된 벽화를 비춥니다.");
		log.add(player + "(은)는 무너진 기둥과 깨진 돌더미를 조심스럽게 피해, 고요히 숨겨져 있던 방에 발을 들입니다.");

		log.add("방 중앙에 놓인 정교하게 조각된 고대의 보물 상자는 그 존재 자체로 위엄과 신비로움을 풍기고 있습니다.");

		List<Item> weaponList = itemRepository.findAllByItemCategory(ItemCategory.WEAPON);

		int randomIndex = ThreadLocalRandom.current().nextInt(weaponList.size());

		Item item = weaponList.get(randomIndex);

		String grade = item.getGrade().getGrade();
		String weaponName = item.getName();

		if (inventory.getWeapons().contains(item.getId())) {
			character.earnGold(200L);
			log.add("상자를 조심스레 열어 보았으나, 그 안에 담긴 무기는 이미 " + player + "의 손에 익숙한 물건이었습니다.");
			log.add("대신, 무기 아래서 발견된 고대의 황금 주화 더미가 반짝이며 " + player + "의 눈을 사로잡습니다. (+200골드)");
		} else {
			inventory.addItem(item.getItemType(), item.getId());
			log.add(player + "(은)는 두근거리는 마음으로 상자의 뚜껑을 천천히 엽니다. 그 순간, (" + grade + " : " + weaponName
				+ ")이 은은하고 신비한 빛과 함께 모습을 드러냅니다!");
			log.add("그 희귀한 무기의 광채가 방 전체를 밝히며, " + player + "의 가슴을 기쁨과 흥분으로 가득 채웁니다.");
		}
		log.setIsPositive(true);
	}
}
