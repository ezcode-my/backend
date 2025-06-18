package org.ezcode.codetest.domain.game.model.encounter;

import java.util.ArrayList;
import java.util.List;

import org.ezcode.codetest.common.base.entity.BaseEntity;
import org.ezcode.codetest.domain.game.model.character.GameCharacter;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BattleHistory extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attacker_id", nullable = false)
	private GameCharacter attacker;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "defender_id", nullable = false)
	private GameCharacter defender;

	@ElementCollection(fetch = FetchType.LAZY)
	private List<String> battleLog = new ArrayList<>();

	private Boolean isAttackerWin;

	@Builder
	public BattleHistory(GameCharacter attacker, GameCharacter defender, List<String> battleLog, Boolean isAttackerWin) {
		this.attacker = attacker;
		this.defender = defender;
		this.battleLog = battleLog;
		this.isAttackerWin = isAttackerWin;
	}
}
