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

		if (!rollHit(attacker, defender, log))
			return true;

		if (RNG.nextDouble() < 0.08) {
			double dealt = attacker.getAtk() * 100;
			boolean alive = defender.playerDamaged(dealt);
			log.add("[%s] 치명적인 일격이 %s의 심장을 꿰뚫었습니다. %,.1f 피해.", attacker.getName(), defender.getName(), dealt);
			log.add("%s이(가) %s를 즉시 쓰러뜨렸습니다!", attacker.getName(), defender.getName());
			return false;
		}
		log.add("%s의 처형 시도! 그러나 실패했습니다.", attacker.getName());
		return fallback.useSkill(attacker, defender, log, weaponType);
	}
}

