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

		boolean isCrit = RNG.nextDouble() * 100 < attacker.getCrit();
		double base = attacker.getAtk() * 0.85 - defender.getDef();
		double total = base * apLeft * (isCrit ? CRIT_MULTIPLIER : 1.0);
		boolean alive = defender.playerDamaged(Math.max(total, 0.0));

		if (isCrit) {
			log.add("치명타 발생. %s의 총구에서 지옥이 열렸습니다.", attacker.getName());
			log.add("%s의 버스트 어택! %d AP를 전부 쏟아붓고, %,.1f의 살상력을 기록했습니다. %s는 산탄으로 갈려나갔습니다.", attacker.getName(), apLeft, total - defender.getDef(), defender.getName());
		} else {
			log.add("%s의 버스트 어택! %d AP를 쏟아부었습니다. 피해량: %,.1f — 적은 마치 샷건과 포옹한 것처럼 보입니다.", attacker.getName(), apLeft, total - defender.getDef());
		}

		if (RNG.nextDouble() * 100 < attacker.getStun()) {
			defender.consumeActionPoints();
			log.add("스턴 발생. %s의 뇌세포가 순간 정지. AP 1 감소 → 남은 AP %d", defender.getName(), defender.getAp());
		}

		log.add("[%s] HP: %,.1f, [%s] HP: %,.1f", attacker.getName(), attacker.getHp(), defender.getName(), defender.getHp());

		if (!alive) {
			log.add("%s이(가) %s를 산탄으로 환자 등록 완료.", attacker.getName(), defender.getName());
			log.add("전투 결과: %s 승리!", attacker.getName());
		}
		return alive;
	}

	@Override
	protected boolean rollHit(CharacterContext attacker, CharacterContext defender, BattleLog log) {
		double hitChance = BASE_HIT_RATE + (attacker.getAccuracy() - defender.getEvasion());
		if (RNG.nextDouble() * 100 >= hitChance) {
			log.add("%s의 총알은 %s를 비껴갔습니다. 심지어 벽도 안 맞았어요.", attacker.getName(), defender.getName());
			attacker.consumeActionPoints();
			return false;
		}
		return true;
	}
}

