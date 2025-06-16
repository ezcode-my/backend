package org.ezcode.codetest.domain.game.model.vo;

import org.ezcode.codetest.domain.game.model.entity.CharacterRealStat;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CharacterContext {

	private String name;
	private Double atk;
	private Double def;
	private Double speed;
	private Double crit;
	private Double stun;
	private Double evasion;
	private Double accuracy;
	private Double hp;
	private Integer ap;

	public static CharacterContext from(String name, CharacterRealStat character) {

		return CharacterContext.builder()
			.name(name)
			.atk(character.getAtk())
			.def(character.getDef())
			.speed(character.getSpeed())
			.crit(character.getCrit())
			.stun(character.getStun())
			.evasion(character.getEvasion())
			.hp(character.getHp())
			.ap(character.getAp())
			.accuracy(character.getAccuracy())
			.build();
	}

	public boolean playerDamaged(Double enemyAtk) {

		enemyAtk -= def;

		if(enemyAtk < 0.0) {
			enemyAtk = 0.0;
		}

		hp -= enemyAtk;

		return hp > 0;
	}

	public boolean checkSpeed(Double enemySpeed) {

		return speed >= enemySpeed;
	}

	public boolean consumeActionPoints() {

		ap--;

		return ap > 0;
	}

	public boolean checkActionPoints() {

		return ap > 0;
	}

 }



