package org.ezcode.codetest.domain.game.strategy.SkillStrategyImpl;

import org.ezcode.codetest.domain.game.model.skill.SkillEffect;
import org.ezcode.codetest.domain.game.model.Encounter.BattleLog;
import org.ezcode.codetest.domain.game.model.Encounter.CharacterContext;
import org.ezcode.codetest.domain.game.strategy.SkillStrategy;
import org.springframework.stereotype.Component;

@Component
public class NoSkill implements SkillStrategy {

	@Override
	public SkillEffect getType() {
		return SkillEffect.NO_SKILL;
	}

	@Override
	public boolean useSkill(CharacterContext player, CharacterContext enemy, BattleLog log) {
		player.consumeActionPoints();
		double rawDamage = player.getAtk();
		double damageDealt = Math.max(rawDamage - enemy.getDef(), 0.0);
		boolean alive = enemy.playerDamaged(rawDamage);
		log.add(
			"%s의 일반 공격! %s에게 %,.1f의 피해를 입혔다. %s는 %s.",
			player.getName(),
			enemy.getName(),
			damageDealt,
			enemy.getName(),
			alive ? "살아남았다" : "쓰러졌다"
		);
		if (!alive) {
			log.add(
				"%s이(가) %s를 쓰러뜨렸습니다!",
				player.getName(),
				enemy.getName()
			);
		}
		return alive;
	}
}
