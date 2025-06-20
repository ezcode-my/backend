package org.ezcode.codetest.domain.game.model.encounter;

import org.ezcode.codetest.common.base.entity.BaseEntity;
import org.ezcode.codetest.domain.game.model.character.GameCharacter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EncounterHistory extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "game_character_id", nullable = false)
	private GameCharacter character;

	@Lob
	@Column(columnDefinition = "TEXT", nullable = false)
	private String resultLog;

	@Column(nullable = false)
	private Boolean isPositive;

	@Builder
	public EncounterHistory(GameCharacter character, String resultLog, Boolean isPositive) {
		this.character = character;
		this.resultLog = resultLog;
		this.isPositive = isPositive;
	}
}
