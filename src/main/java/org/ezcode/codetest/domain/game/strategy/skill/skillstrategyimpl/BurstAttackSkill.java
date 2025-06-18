package org.ezcode.codetest.domain.game.strategy.skill.skillstrategyimpl;

import org.ezcode.codetest.domain.game.model.encounter.BattleLog;
import org.ezcode.codetest.domain.game.model.encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.item.WeaponType;
import org.ezcode.codetest.domain.game.model.skill.SkillEffect;
import org.springframework.stereotype.Component;

@Component
public class BurstAttackSkill extends AbstractSkill {

	@Override
	public SkillEffect getType() { return SkillEffect.BURST_ATTACK; }

	@Override
	public boolean useSkill(CharacterContext attacker, CharacterContext defender, BattleLog log, WeaponType weaponType) {

		if (!rollHit(attacker, defender, log)) return true;

		int apLeft = attacker.getAp();
		for (int i = 0; i < apLeft; i++) {
			attacker.consumeActionPoints();
		}

		double base = attacker.getAtk() * 0.75;
		double total = base * apLeft;
		boolean alive = defender.playerDamaged(Math.max(total, 0.0));

		log.add("%s의 버스트 어택! %d AP 사용, %,.1f 피해.", attacker.getName(), apLeft, total - defender.getDef());

		if (RNG.nextDouble() * 100 < attacker.getStun()) {
			defender.consumeActionPoints();
			log.add("스턴! %s의 행동력 1 감소 -> %d", defender.getName(), defender.getAp());
		}

		log.add("[%s] HP: %,.1f, [%s] HP: %,.1f", attacker.getName(), attacker.getHp(), defender.getName(), defender.getHp());

		if (!alive) {
			log.add("%s이(가) %s를 쓰러뜨렸습니다!", attacker.getName(), defender.getName());
		}
		return alive;
	}
}
