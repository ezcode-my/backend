package org.ezcode.codetest.domain.game.model.entity;

import org.ezcode.codetest.common.base.entity.BaseEntity;

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
public class Battle extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attacker_id")
	private GameCharacter attacker;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "defender_id")
	private GameCharacter defender;

	private String battleLog;

	private Boolean isWinAttacker;

	@Builder
	public Battle(GameCharacter attacker, GameCharacter defender, String battleLog, Boolean isWinAttacker) {
		this.attacker = attacker;
		this.defender = defender;
		this.battleLog = battleLog;
		this.isWinAttacker = isWinAttacker;
	}
}
