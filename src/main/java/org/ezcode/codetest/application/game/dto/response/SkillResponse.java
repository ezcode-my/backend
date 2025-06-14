package org.ezcode.codetest.application.game.dto.response;

import org.ezcode.codetest.domain.game.model.entity.Skill;
import org.ezcode.codetest.domain.game.model.enums.Grade;
import org.ezcode.codetest.domain.game.model.enums.SkillEffect;

import lombok.Builder;

@Builder
public record SkillResponse (

	SkillEffect skillEffect,

	Grade grade,

	String name,

	String skillDetails

) {
	public static SkillResponse from(Skill skill) {

		return SkillResponse.builder()
			.grade(skill.getGrade())
			.name(skill.getName())
			.skillDetails(skill.getSkillDetails())
			.skillEffect(skill.getSkillEffect())
			.build();
	}
}
