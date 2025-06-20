package org.ezcode.codetest.domain.game.strategy.skill.skillstrategyimpl;

import org.ezcode.codetest.domain.game.model.encounter.BattleLog;
import org.ezcode.codetest.domain.game.model.encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.item.WeaponType;
import org.ezcode.codetest.domain.game.model.skill.SkillEffect;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InstantKillSkill extends AbstractSkill {

	private final NoSkill fallback;

	@Override
	public SkillEffect getType() {
		return SkillEffect.INSTANT_KILL;
	}

	@Override
	public boolean useSkill(CharacterContext attacker, CharacterContext defender, BattleLog log,
		WeaponType weaponType) {

		if (RNG.nextDouble() < 0.08) {
			double dealt = attacker.getAtk() * 100;
			boolean alive = defender.playerDamaged(dealt);
			log.add("[%s] 침묵의 한 방이 %s의 심장을 꿰뚫었습니다 %,.1f 피해.", attacker.getName(), defender.getName(), dealt);
			log.add("%s은(는) 그 자리에서 죽음을 맞이했습니다. 다음 희생자를 찾으세요.", defender.getName());
			log.add("%s는 또 한 명의 영혼을 수확했습니다.", attacker.getName());
			return false;
		}

		log.add("%s가 처형을 시도 했지만, %s는 운 좋게도 한 발자국 비켜섰습니다. - 명중 실패", attacker.getName(), defender.getName());
		return fallback.useSkill(attacker, defender, log, weaponType);
	}

}

