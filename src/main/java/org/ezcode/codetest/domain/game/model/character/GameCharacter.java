package org.ezcode.codetest.domain.game.model.character;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.ezcode.codetest.common.base.entity.BaseEntity;
import org.ezcode.codetest.domain.game.exception.GameException;
import org.ezcode.codetest.domain.game.exception.GameExceptionCode;
import org.ezcode.codetest.domain.game.model.item.AccessoryType;
import org.ezcode.codetest.domain.game.model.item.DefenceType;
import org.ezcode.codetest.domain.game.model.item.ItemType;
import org.ezcode.codetest.domain.game.model.item.WeaponType;
import org.ezcode.codetest.domain.user.model.entity.User;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
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

	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false, unique = true)
	private User user;

	@ElementCollection(fetch = FetchType.LAZY)
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

	private String weaponId;
	private String defenceId;
	private String accessoryId;

	private long gold;

	public GameCharacter(User user) {

		this.user = user;
		for (Stat stat : Stat.values()) {
			stats.put(stat, 0.0);
		}

		this.name = user.getNickname();

		gold = 10000L;
		this.weaponId = WeaponType.NOTHING.name();
		this.defenceId = DefenceType.NOTHING.name();
		this.accessoryId = AccessoryType.NOTHING.name();
		realStat = new CharacterRealStat();
	}

	public void applyIncreaseStats(Map<Stat, Double> stats) {

		stats.forEach((stat, rate) -> this.stats.merge(stat, rate, Double::sum));
		realStat.applyIncreaseRealStats(stats);
	}

	public void useGoldForGamble() {

		if(gold < 50L) throw new GameException(GameExceptionCode.NOT_ENOUGH_GOLD);

		gold -= 50L;
	}

	public void earnGold(Long gold) {

		this.gold += gold;
	}

	public void equipItem(ItemType item , String newItem) {

		if (item instanceof WeaponType) {
			weaponId = newItem;
		} else if (item instanceof DefenceType) {
			defenceId = newItem;
		} else if (item instanceof AccessoryType) {
			accessoryId = newItem;
		}
	}

	public List<String> unEquipAllItems() {

		weaponId = WeaponType.NOTHING.name();
		defenceId = DefenceType.NOTHING.name();
		accessoryId = AccessoryType.NOTHING.name();
		return List.of(weaponId, defenceId, accessoryId);
	}
}
