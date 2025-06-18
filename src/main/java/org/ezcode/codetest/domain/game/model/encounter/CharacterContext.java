package org.ezcode.codetest.domain.game.model.encounter;

import org.ezcode.codetest.domain.game.model.character.CharacterRealStat;

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

		if (enemyAtk < 0.0) {
			enemyAtk = 0.0;
		}

		hp -= enemyAtk;

		return hp > 0;
	}

	public void restoreHp(Double hp) {

		this.hp += hp;
	}

	public void applyCritBuff(Double crit) {

		this.crit += crit;
	}

	public void applyAtkBuff(Double atk) {

		this.atk += atk;
	}

	public void applyAtkDebuff(Double atk) {

		this.atk = (this.atk - atk) >= 0 ? (this.atk - atk) : 0;
	}

	public void applyEvasionBuff(Double evasion) {

		this.evasion += evasion;
	}

	public void applyAccuracyBuff(Double accuracy) {

		this.accuracy += accuracy;
	}

	public void applyAccuracyDebuff(Double accuracy) {

		this.accuracy = (this.accuracy - accuracy) >= 0 ? (this.accuracy - accuracy) : 0;
	}

	public void applyDefBuff(Double def) {

		this.def += def;
	}

	public void applyStunBuff(Double stun) {

		this.stun += stun;
	}

	public boolean checkSpeed(Double enemySpeed) {

		return speed >= enemySpeed;
	}

	public boolean consumeActionPoints() {

		if(ap <= 0) {

			return false;
		}

		ap--;

		return true;
	}

	public boolean checkActionPoints() {

		return ap > 0;
	}

}



