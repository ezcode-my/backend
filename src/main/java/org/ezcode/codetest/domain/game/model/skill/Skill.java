package org.ezcode.codetest.domain.game.model.skill;

import org.ezcode.codetest.common.base.entity.BaseEntity;
import org.ezcode.codetest.domain.game.model.item.Grade;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Skill extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private SkillEffect skillEffect;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Grade grade;

	@Column(nullable = false, unique = true)
	private String name;

	@Column(nullable = false)
	private String skillDetails;

	@Builder
	public Skill(SkillEffect skillEffect, Grade grade, String name, String skillDetails) {

		this.skillEffect = skillEffect;
		this.grade = grade;
		this.name = name;
		this.skillDetails = skillDetails;
	}
}
