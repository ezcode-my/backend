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
public class WildBeastsAttack implements EncounterStrategy {

	@Override
	public RandomEncounterEffect getEffect() {
		return RandomEncounterEffect.WILD_BEASTS_ATTACK;
	}

	@Override
	public void eventHappen(
		GameCharacter character,
		Inventory inventory,
		CharacterContext context,
		EncounterLog log
	) {
		CharacterRealStat real = character.getRealStat();
		String player = context.getName();

		double atk = context.getAtk();
		double accuracy = context.getAccuracy();
		double chance = Math.min((atk + accuracy) / 2.0 / 100.0, 1.0);

		log.add("%s(은)는 오늘만은 도망치지 않기로 결심했습니다. 물론, 뇌는 도망을 원했고 장기는 반란을 준비했지만요.", player);
		log.add("그 선택은 왜였을까요? 사실 그것은 %s의 의지가 아니라, 화면 앞에서 커피를 마시던 취준생의 무심한 마우스 클릭 때문이었습니다.", player);
		log.add("울창한 숲 속, 짙은 안개 너머로 굶주린 맹수의 으르렁거림이 귓가를 울립니다.");
		log.add("%s(은)는 한숨을 쉬며 무기를 꺼냅니다. 이젠 동물도 말로 설득할 수 없다는 걸 압니다.", player);
		log.add("이런저런 잡생각이 스치던 찰나, 돌연변이 늑대가 포효와 함께 돌진해왔고, %s(은)는 반사적으로 무기를 휘둘렀습니다.", player);

		boolean victory = ThreadLocalRandom.current().nextDouble() < chance;

		if (victory) {
			real.applyDefChange(0.5);
			log.add("그 일격은 생각 이상으로 정확했고, 돌연변이 늑대는 첫 타에 정신을 잃었습니다.");
			log.add("%s(은)는 그 사체에서 쓸만한 가죽만 벗겨갑니다.", player);
			log.add("싸움은 짧았고, 생존자는 당신뿐이었습니다.");
			log.add("누군가는 이걸 ‘자연의 섭리’라 하겠지만, 그냥 구질구질한 생존일 뿐입니다. (방어력 +0.5)");
			log.setIsPositive(true);
		} else {
			real.applySpeedChange(-0.5);
			log.add("하지만 돌연변이 늑대는 마지막 순간 몸을 비틀어 %s의 허벅지를 노렸습니다.", player);
			log.add("송곳니가 살을 찢고 들어오며, %s(은)는 반사적으로 뒤로 물러섰지만 이미 늦었습니다.", player);
			log.add("순식간의 싸움. 그러나 결과는 당신에게 너무도 길게 남을 것입니다.");
			log.add("고통 속에 %s(은)는 비명을 지르며 언덕을 굴러 도망쳤습니다. 다시는 사료 광고를 믿지 않겠다고 다짐하면서.", player);
			log.add("스피드는 줄었고, 반사신경은 고장났습니다. 축하합니다—이제 당신은 늑대보다 느린 인간 대표입니다. (스피드 -0.5)");
			log.setIsPositive(false);
		}
	}
}
