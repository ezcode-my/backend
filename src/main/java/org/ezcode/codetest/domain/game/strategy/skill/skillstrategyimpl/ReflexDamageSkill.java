package org.ezcode.codetest.domain.game.strategy.skill.skillstrategyimpl;

import org.ezcode.codetest.domain.game.model.encounter.BattleLog;
import org.ezcode.codetest.domain.game.model.encounter.CharacterContext;
import org.ezcode.codetest.domain.game.model.item.WeaponType;
import org.ezcode.codetest.domain.game.model.skill.SkillEffect;
import org.springframework.stereotype.Component;

@Component
public class ReflexDamageSkill extends AbstractSkill {

	@Override
	public SkillEffect getType() {
		return SkillEffect.REFLEX_DAMAGE;
	}

	@Override
	public boolean useSkill(CharacterContext attacker,
		CharacterContext defender,
		BattleLog log,
		WeaponType weaponType) {

		attacker.consumeActionPoints();

		if (!rollHit(attacker, defender, log)) {
			return true;
		}

		double baseReflex = defender.getAtk() * 1.5;

		boolean isCrit = RNG.nextDouble() * 100 < attacker.getCrit();
		double rawReflex = baseReflex * (isCrit ? CRIT_MULTIPLIER : 1.0);

		boolean alive = defender.playerDamaged(rawReflex + defender.getDef());

		if (isCrit) {
			log.add("%s의 치명적 반사 피해. 상대 공격력의 150%%(%,.1f) ×%.1f배 → %,.1f 피해를 입혔습니다. 맞으면서도 '내가 왜 맞지?' 생각했겠죠.",
				attacker.getName(), baseReflex, CRIT_MULTIPLIER, Math.max(rawReflex, 0.0));
		} else {
			log.add("%s의 반사 피해! 상대 공격력의 150%%(%,.1f) 만큼 → %,.1f 피해를 입혔습니다. 맞으면서도 '내가 왜 맞지?' 생각했겠죠.",
				attacker.getName(), baseReflex, Math.max(rawReflex, 0.0));
		}

		if (RNG.nextDouble() * 100 < attacker.getStun()) {
			defender.consumeActionPoints();
			log.add("스턴 발생. %s의 행동력 1 감소 → %d. 남은 AP %d", defender.getName(), defender.getAp(), defender.getAp());
		}

		double baseSelf   = defender.getAtk() * 0.5;
		double currentHp  = attacker.getHp();
		double netSelf    = Math.max(baseSelf - attacker.getDef(), 0);
		double actualSelf = Math.min(netSelf, currentHp - 1);
		attacker.playerDamaged(actualSelf + attacker.getDef());
		log.add("반사 피해 대가로 자신은 %,.1f만큼 피해를 입었습니다. 고통도 명예의 일부입니다.", actualSelf);

		log.add("[%s] HP: %,.1f | [%s] HP: %,.1f",
			attacker.getName(), attacker.getHp(),
			defender.getName(), defender.getHp());

		if (!alive) {
			log.add("%s은(는) 반사된 힘에 의해 완전히 제압당했습니다.", defender.getName());
			log.add("%s이(가) %s를 쓰러뜨렸습니다.",
				attacker.getName(), defender.getName());
		}

		return alive;
	}

	@Override
	protected boolean rollHit(CharacterContext attacker, CharacterContext defender, BattleLog log) {
		double hitChance = BASE_HIT_RATE + (attacker.getAccuracy() - defender.getEvasion());
		boolean hit = RNG.nextDouble() * 100 < hitChance;
		if (!hit) {
			log.add("%s의 반사 공격이 빗나가고, %s(은)는 아직도 왜 살아있는지 본인도 모릅니다.", attacker.getName(), defender.getName());
		}
		return hit;
	}
}
