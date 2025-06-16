package org.ezcode.codetest.application.game.dto.response;

import org.ezcode.codetest.domain.game.model.entity.GameCharacterSkill;
import org.ezcode.codetest.domain.game.model.entity.Skill;
import org.ezcode.codetest.domain.game.model.enums.Grade;
import org.ezcode.codetest.domain.game.model.enums.SkillEffect;
import org.ezcode.codetest.domain.game.model.enums.SkillSlotType;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SkillResponse (

	SkillEffect skillEffect,

	Grade grade,

	String name,

	String skillDetails,

	SkillSlotType slotType

) {
	public static SkillResponse from(Skill skill) {

		return SkillResponse.builder()
			.grade(skill.getGrade())
			.name(skill.getName())
			.skillDetails(skill.getSkillDetails())
			.skillEffect(skill.getSkillEffect())
			.build();
	}

	public static SkillResponse from(GameCharacterSkill characterSkill) {

		Skill skill = characterSkill.getSkill();

		return SkillResponse.builder()
			.grade(skill.getGrade())
			.name(skill.getName())
			.skillDetails(skill.getSkillDetails())
			.skillEffect(skill.getSkillEffect())
			.slotType(characterSkill.getSlotType())
			.build();
	}
}
