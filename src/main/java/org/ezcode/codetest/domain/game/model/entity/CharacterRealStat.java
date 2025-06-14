package org.ezcode.codetest.domain.game.model.entity;

import java.util.List;
import java.util.Map;

import org.ezcode.codetest.domain.game.model.enums.ItemCategory;
import org.ezcode.codetest.domain.game.model.enums.Stat;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CharacterRealStat {

	private Double atk = 5.0;
	private Double def = 5.0;
	private Double speed = 5.0;
	private Double crit = 5.0;
	private Double stun = 5.0;
	private Double evasion = 5.0;
	private Double accuracy = 5.0;
	private Double hp = 50.0;
	private Integer ap = 3;

	public CharacterRealStat(CharacterRealStat source) {
		this.atk      = source.atk;
		this.def      = source.def;
		this.speed    = source.speed;
		this.crit     = source.crit;
		this.stun     = source.stun;
		this.evasion  = source.evasion;
		this.accuracy = source.accuracy;
		this.hp       = source.hp;
		this.ap       = source.ap;
	}

	public void applyItemRealStat(List<Item> equippedItems) {

		if (equippedItems == null || equippedItems.isEmpty()) {
			return;
		}

		equippedItems.forEach(item -> {
				if(item instanceof Weapon weapon) {
					this.atk += weapon.getAtk();
					this.speed += weapon.getSpeed();
					this.crit += weapon.getCrit();
					this.stun += weapon.getStun();
					this.accuracy += weapon.getAccuracy();
				} else if(item instanceof Defence defence) {
					this.def += defence.getDef();
					this.speed += defence.getSpeed();
					this.evasion += defence.getEvasion();
				} else if(item instanceof Accessory accessory) {
					this.speed += accessory.getSpeed();
					this.crit += accessory.getCrit();
					this.stun += accessory.getStun();
					this.evasion += accessory.getEvasion();
					this.accuracy += accessory.getAccuracy();
				}
			}
		);
	}

	public void applyIncreaseRealStats(Map<Stat, Double> increaseRates) {
		increaseRates.forEach(this::increase);
	}

	public void increase(Stat stat, double rate) {
		switch(stat) {
			case PROBLEM_SOLVING:
				this.atk += 2 + rate;
				break;
			case DATA_STRUCTURE:
				this.def += 1 + rate;
				this.atk += 0.5 + rate;
				break;
			case SPEED:
				this.speed += 1 + rate;
				this.atk += rate;
				break;
			case DEBUGGING:
				this.crit += rate;
				this.stun += rate;
				break;
			case OPTIMIZATION:
				this.evasion += 1 + rate;
				this.accuracy += 1 + rate;
				break;
			default:
				break;
		}
	}
}
