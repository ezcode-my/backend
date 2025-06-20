package org.ezcode.codetest.domain.game.strategy.encounter.encounterstrategyimpl;

import java.util.concurrent.ThreadLocalRandom;

import org.ezcode.codetest.domain.game.model.character.CharacterRealStat;
import org.ezcode.codetest.domain.game.model.character.GameCharacter;
import org.ezcode.codetest.domain.game.model.character.Inventory;
import org.ezcode.codetest.domain.game.model.encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.encounter.EncounterLog;
import org.ezcode.codetest.domain.game.model.encounter.RandomEncounterEffect;
import org.ezcode.codetest.domain.game.strategy.encounter.EncounterStrategy;
import org.springframework.stereotype.Component;

@Component
public class StatSpeed implements EncounterStrategy {

	@Override
	public RandomEncounterEffect getEffect() {
		return RandomEncounterEffect.STAT_SPEED;
	}

	@Override
	public void eventHappen(GameCharacter character, Inventory inventory, CharacterContext context, EncounterLog log) {

		CharacterRealStat realStat = character.getRealStat();
		String player = context.getName();

		double speedChange = ThreadLocalRandom.current().nextBoolean() ? 0.5 : -0.5;
		realStat.applySpeedChange(speedChange);

		log.add("%s(은)는 눅눅하고 어두운 복도를 따라 조심스레 발걸음을 옮겼습니다. 공기 중엔 녹슨 금속과 오래된 땀 냄새가 뒤섞여 코를 자극하죠.", player);
		log.add("발밑에서 들리는 삐걱거림이 ‘여긴 잘못 들어왔다’고 소리치지만, 이미 뒤로 물러설 곳은 없습니다.");
		log.add("복도 끝에서 희미하게 빛나는 병 하나를 발견했습니다. 녹슨 뚜껑은 마치 ‘이걸 마시면… 글쎄요’라고 경고하는 듯했습니다.");
		log.add("%s(은)는 ‘별수 있나’ 싶어 병뚜껑을 열고 한 모금 들이켰습니다. 이제 결과는 순전히 운에 맡겨야죠.", player);

		if (speedChange > 0) {
			log.add("갑자기 몸에 힘이 솟구쳤습니다. 움직임이 가벼워지고, ‘이 복도쯤이야 뛰어넘어야지’ 하는 기분이 들었습니다. (스피드 +0.5)");
			log.setIsPositive(true);
		} else {
			log.add("갑자기 몸이 무겁고 둔해졌습니다. ‘이게 축복이라면 난 저주를 택하겠다’라는 생각이 스쳤죠. (스피드 -0.5)");
			log.setIsPositive(false);
		}
	}
}