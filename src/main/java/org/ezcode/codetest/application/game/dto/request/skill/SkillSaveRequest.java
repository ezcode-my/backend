package org.ezcode.codetest.application.game.dto.request.skill;

import org.ezcode.codetest.common.validation.EnumValidator;
import org.ezcode.codetest.domain.game.model.skill.Skill;
import org.ezcode.codetest.domain.game.model.item.Grade;
import org.ezcode.codetest.domain.game.model.skill.SkillEffect;

import jakarta.validation.constraints.NotBlank;

public record SkillSaveRequest(

	@NotBlank(message = "스킬 이펙트를 입력해주세요.")
	@EnumValidator(enumClass = SkillEffect.class)
	String skillEffect,

	@NotBlank(message = "등급을 입력해주세요.")
	@EnumValidator(enumClass = Grade.class)
	String grade,

	@NotBlank(message = "스킬 이름을 입력해주세요.")
	String name,

	@NotBlank(message = "스킬 설명을 입력해주세요.")
	String skillDetails
) {
	public Skill toSkill() {
		return Skill.builder()
			.skillEffect(SkillEffect.valueOf(skillEffect.trim().toUpperCase()))
			.grade(Grade.valueOf(grade.trim().toUpperCase()))
			.name(name)
			.skillDetails(skillDetails)
			.build();
	}
}
