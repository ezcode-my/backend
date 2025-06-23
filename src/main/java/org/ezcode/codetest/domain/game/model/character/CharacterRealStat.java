package org.ezcode.codetest.domain.game.model.character;

import java.util.List;
import java.util.Map;

import org.ezcode.codetest.domain.game.model.item.Accessory;
import org.ezcode.codetest.domain.game.model.item.Defence;
import org.ezcode.codetest.domain.game.model.item.Item;
import org.ezcode.codetest.domain.game.model.item.Weapon;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CharacterRealStat {

	private double atk = 5.0;
	private double def = 5.0;
	private double speed = 5.0;
	private double crit = 5.0;
	private double stun = 5.0;
	private double evasion = 5.0;
	private double accuracy = 5.0;
	private double hp = 50.0;
	private int ap = 3;

	public CharacterRealStat(CharacterRealStat source) {
		this.atk = source.atk;
		this.def = source.def;
		this.speed = source.speed;
		this.crit = source.crit;
		this.stun = source.stun;
		this.evasion = source.evasion;
		this.accuracy = source.accuracy;
		this.hp = source.hp;
		this.ap = source.ap;
	}

	public void applyItemRealStat(List<Item> equippedItems) {

		if (equippedItems == null || equippedItems.isEmpty()) {
			return;
		}

		equippedItems.forEach(item -> {
				if (item instanceof Weapon weapon) {
					this.atk += weapon.getAtk();
					this.speed += weapon.getSpeed();
					this.crit += weapon.getCrit();
					this.stun += weapon.getStun();
					this.accuracy += weapon.getAccuracy();
				} else if (item instanceof Defence defence) {
					this.def += defence.getDef();
					this.speed += defence.getSpeed();
					this.evasion += defence.getEvasion();
				} else if (item instanceof Accessory accessory) {
					this.speed += accessory.getSpeed();
					this.crit += accessory.getCrit();
					this.stun += accessory.getStun();
					this.evasion += accessory.getEvasion();
					this.accuracy += accessory.getAccuracy();
				}
			}
		);
	}

	public double statSummary() {

		return atk + def + speed + crit + stun + evasion + accuracy;
	}

	public void applyIncreaseRealStats(Map<Stat, Double> increaseRates) {
		increaseRates.forEach(this::increase);
	}

	public void increase(Stat stat, double rate) {
		switch (stat) {
			case PROBLEM_SOLVING:
				this.atk += rate / 10;
				this.accuracy += rate / 10;
				break;
			case DATA_STRUCTURE:
				this.def += rate / 10;
				this.atk += rate / 10;
				this.accuracy += rate / 5;
				break;
			case SPEED:
				this.speed += rate / 5;
				this.atk += rate / 10;
				this.accuracy += rate / 5;
				break;
			case DEBUGGING:
				this.crit += rate / 5;
				this.def += rate / 10;
				this.stun += rate / 10;
				break;
			case OPTIMIZATION:
				this.evasion += rate / 5;
				this.def += rate / 10;
				this.accuracy += rate / 5;
				break;
			default:
				break;
		}
	}

	public void applyAtkChange(double atk) {

		this.atk += atk;
		if (this.atk < 0.0)
			this.atk = 0.0;
	}

	public void applyDefChange(double def) {

		this.def += def;
		if (this.def < 0.0)
			this.def = 0.0;
	}

	public void applySpeedChange(double speed) {

		this.speed += speed;
		if (this.speed < 0.0)
			this.speed = 0.0;
	}

	public void applyCritChange(double crit) {

		this.crit += crit;
		if (this.crit < 0.0)
			this.crit = 0.0;
	}

	public void applyEvasionChange(double evasion) {

		this.evasion += evasion;
		if (this.evasion < 0.0)
			this.evasion = 0.0;
	}

	public void applyAccuracyChange(double accuracy) {

		this.accuracy += accuracy;
		if (this.accuracy < 0.0)
			this.accuracy = 0.0;
	}

	public void applyStunChange(double stun) {

		this.stun += stun;
		if (this.stun < 0.0)
			this.stun = 0.0;
	}

}
