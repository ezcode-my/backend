package org.ezcode.codetest.application.game.dto.request;

import org.ezcode.codetest.domain.game.model.entity.Skill;
import org.ezcode.codetest.domain.game.model.enums.Grade;
import org.ezcode.codetest.domain.game.model.enums.SkillEffect;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SkillSaveRequest(

	@NotBlank(message = "스킬 이펙트를 입력해주세요.")
	@Pattern(
		regexp = "LIFE_STEAL|COUNTER_ATTACK|REFLEX_DAMAGE|HEAL|BURST_ATTACK|INSTANT_KILL|BLOODY_MESS|DEFENCE|ILLUSION",
		flags = Pattern.Flag.CASE_INSENSITIVE,
		message = "스킬 이펙트는 LIFE_STEAL, COUNTER_ATTACK, REFLEX_DAMAGE, HEAL, BURST_ATTACK, INSTANT_KILL, BLOODY_MESS, DEFENCE, ILLUSION 중 하나여야 합니다."
	)
	String skillEffect,

	@NotBlank(message = "등급을 입력해주세요.")
	@Pattern(
		regexp = "LEGENDARY|UNIQUE|RARE|UNCOMMON|COMMON|TRASH",
		flags = Pattern.Flag.CASE_INSENSITIVE,
		message = "아이템 등급은 LEGENDARY, UNIQUE, RARE, UNCOMMON, COMMON, TRASH 중 하나여야 합니다."
	)
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
