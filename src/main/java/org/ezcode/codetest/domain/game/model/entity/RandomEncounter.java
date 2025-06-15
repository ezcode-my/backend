package org.ezcode.codetest.domain.game.model.entity;

import org.ezcode.codetest.common.base.entity.BaseEntity;
import org.ezcode.codetest.domain.game.model.enums.EncounterCategory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class RandomEncounter extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private EncounterCategory encounterCategory;

	@Column(nullable = false, unique = true)
	private String name;

	@Column(nullable = false)
	private String encounterText;

	@Column(nullable = false)
	private Boolean isActivated;

	@Column(nullable = false)
	private Integer weight;
}
