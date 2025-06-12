package org.ezcode.codetest.domain.game.model.entity;

import java.util.EnumMap;
import java.util.Map;

import org.ezcode.codetest.common.base.entity.BaseEntity;
import org.ezcode.codetest.domain.game.exception.GameException;
import org.ezcode.codetest.domain.game.exception.GameExceptionCode;
import org.ezcode.codetest.domain.game.model.enums.Accessory;
import org.ezcode.codetest.domain.game.model.enums.Defence;
import org.ezcode.codetest.domain.game.model.enums.Item;
import org.ezcode.codetest.domain.game.model.enums.Stat;
import org.ezcode.codetest.domain.game.model.enums.Weapon;
import org.ezcode.codetest.domain.user.model.entity.User;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.MapKeyEnumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "game_character")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GameCharacter extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ElementCollection
	@CollectionTable(
		name = "user_stat_value",
		joinColumns = @JoinColumn(name = "game_character_id")
	)
	@MapKeyColumn(name = "stat")
	@Column(name = "value")
	@MapKeyEnumerated(EnumType.STRING)
	private Map<Stat, Double> stats = new EnumMap<>(Stat.class);

	@Embedded
	private CharacterRealStat realStat;
	
	@Enumerated(EnumType.STRING)
	private Weapon weapon;

	@Enumerated(EnumType.STRING)
	private Defence defence;

	@Enumerated(EnumType.STRING)
	private Accessory accessory;

	private Long gold;

	public GameCharacter(User user) {

		this.user = user;
		for (Stat stat : Stat.values()) {
			stats.put(stat, 0.0);
		}
		gold = 100L;
		weapon = Weapon.NOTHING;
		defence = Defence.NOTHING;
		accessory = Accessory.NOTHING;
		realStat = new CharacterRealStat();
	}

	public void applyIncreaseStats(Map<Stat, Double> stats) {

		stats.forEach((stat, rate) -> this.stats.merge(stat, rate, Double::sum));
		realStat.applyIncreaseRealStats(stats);
	}

	public void useGoldForGamble() {

		if(gold < 50L) throw new GameException(GameExceptionCode.NOT_ENOUGH_GOLD);

		gold -= 50L;;
	}

	public void earnGold(Long gold) {

		this.gold += gold;
	}

	public Item equipItem(Item item) {
		Item oldItem = null;

		if (item instanceof Weapon newWeapon) {
			oldItem = weapon;
			weapon = newWeapon;
		} else if (item instanceof Defence newDefence) {
			oldItem = defence;
			defence = newDefence;
		} else if (item instanceof Accessory newAccessory) {
			oldItem = accessory;
			accessory = newAccessory;
		}
		return oldItem;
	}
}
