package org.ezcode.codetest.domain.game.model.encounter;

import org.ezcode.codetest.common.base.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class EncounterChoice extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "random_encounter_id", nullable = false)
	private RandomEncounter encounter;

	@Column(nullable = false, unique = true)
	private String name;

	@Column(nullable = false)
	private String resultText;

	@Enumerated(EnumType.STRING)
	private RandomEncounterEffect encounterEffect;

	@Builder
	public EncounterChoice(
		RandomEncounter encounter,
		String name,
		String resultText,
		RandomEncounterEffect encounterEffect
	) {
		this.encounter = encounter;
		this.name = name;
		this.resultText = resultText;
		this.encounterEffect = encounterEffect;
	}
}
