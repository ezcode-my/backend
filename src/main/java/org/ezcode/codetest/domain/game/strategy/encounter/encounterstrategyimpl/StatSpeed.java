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
			log.add("그리고 마시는 순간 당신은 느꼈습니다. ‘오, 개쩌는데?’");
			log.add("물약은 알고 보니 고대 군사 실험 중 폐기된 ‘순간 근육 흥분제’였습니다. 부작용은... 아마 나중에 올 겁니다. (스피드 +0.5)");
			log.setIsPositive(true);
		} else {
			log.add("그리고 마시는 순간 당신은 느꼈습니다. ‘아… 이거 망했네.’");
			log.add("물약은 사실 포스트 아포칼립스에서 가장 위험한 그것, ‘만병 통치용 파이프 세척제’였습니다. (스피드 -0.5)");
			log.add("%s(은)는 바닥에 주저앉아 속이 뒤집히는 느낌을 음미합니다. ‘아, 이게 진짜 체험학습이구나’", player);
			log.setIsPositive(false);
		}
	}
}