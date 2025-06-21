package org.ezcode.codetest.domain.game.model.encounter;

import org.ezcode.codetest.common.base.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RandomEncounter extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private EncounterCategory encounterCategory;

	@Column(nullable = false, unique = true)
	private String name;

	@Lob
	@Column(columnDefinition = "TEXT", nullable = false)
	private String encounterText;

	@Column(nullable = false)
	private String choice1Text;

	@Column(nullable = false)
	private String choice2Text;

	@Column(nullable = false)
	private boolean activated;

	@Builder
	public RandomEncounter(
		EncounterCategory encounterCategory,
		String name,
		String encounterText,
		String choice1Text,
		String choice2Text,
		boolean activated
	) {
		this.encounterCategory = encounterCategory;
		this.name = name;
		this.encounterText = encounterText;
		this.choice1Text = choice1Text;
		this.choice2Text = choice2Text;
		this.activated = activated;
	}

	public void softDelete() {
		activated = false;
	}
}
