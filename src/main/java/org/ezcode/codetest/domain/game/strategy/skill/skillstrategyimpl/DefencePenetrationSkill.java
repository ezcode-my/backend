package org.ezcode.codetest.domain.game.strategy.skill.skillstrategyimpl;

import org.ezcode.codetest.domain.game.model.encounter.BattleLog;
import org.ezcode.codetest.domain.game.model.encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.item.WeaponType;
import org.ezcode.codetest.domain.game.model.skill.SkillEffect;
import org.springframework.stereotype.Component;

@Component
public class DefencePenetrationSkill extends AbstractSkill {

	@Override
	public SkillEffect getType() {
		return SkillEffect.DEFENCE_PENETRATION;
	}

	@Override
	public boolean useSkill(CharacterContext attacker,
		CharacterContext defender,
		BattleLog log,
		WeaponType weaponType) {

		double penetrationBuff = defender.getDef() * 0.2;
		attacker.applyAtkBuff(penetrationBuff);

		log.add("%s의 방어력 관통 강화. %s의 50%% 방어력을 추가로 더 무시하고 공격을 시작합니다. 방어구? 그저 속 빈 강정일 뿐입니다.",
			attacker.getName(), defender.getName());

		attacker.consumeActionPoints();

		if (!rollHit(attacker, defender, log)) {
			return true;
		}

		boolean isCrit = RNG.nextDouble() * 100 < attacker.getCrit();
		double rawDamage = attacker.getAtk() * (isCrit ? CRIT_MULTIPLIER : 1.0);

		double dealt = Math.max(rawDamage - defender.getDef(), 0.0);
		boolean alive = defender.playerDamaged(rawDamage);

		if (isCrit) {
			log.add("치명타 발생. %s의 철벽도 무용지물이 되어 %,.1f의 피해를 입었습니다.", defender.getName(), dealt);
		} else {
			log.add("%s의 공격이 %s의 방어를 뚫고 %,.1f 피해를 가했습니다.",
				attacker.getName(), defender.getName(), dealt);
		}

		if (RNG.nextDouble() * 100 < attacker.getStun()) {
			defender.consumeActionPoints();
			log.add("스턴 효과! %s의 중심이 흔들리며 행동력이 1 감소했습니다 → 남은 AP %d", defender.getName(), defender.getAp());
		}

		log.add("[%s] HP: %,.1f | [%s] HP: %,.1f",
			attacker.getName(), attacker.getHp(),
			defender.getName(), defender.getHp());

		if (!alive) {
			log.add("%s의 갑옷이 산산조각 났고, %s는 이제 기억 속에만 남았습니다.", defender.getName(), defender.getName());
			log.add("%s가 %s를 완전히 박살냈습니다. 후회는 없습니다.", attacker.getName(), defender.getName());
		}

		return alive;
	}

	@Override
	protected boolean rollHit(CharacterContext attacker, CharacterContext defender, BattleLog log) {
		double hitChance = BASE_HIT_RATE + (attacker.getAccuracy() - defender.getEvasion());
		boolean hit = !(RNG.nextDouble() * 100 >= hitChance);
		if (!hit) {
			log.add("근데 방어력을 무시하면 뭐가 좋은거죠? %s(은)는 비웃으며 옆으로 슬쩍 피했습니다. - 명중 실패.", defender.getName());
		}
		return hit;
	}
}