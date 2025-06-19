package org.ezcode.codetest.application.game.dto.request.skill;

import org.ezcode.codetest.common.validation.EnumValidator;
import org.ezcode.codetest.domain.game.model.skill.Skill;
import org.ezcode.codetest.domain.game.model.item.Grade;
import org.ezcode.codetest.domain.game.model.skill.SkillEffect;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "스킬 저장 요청")
public record SkillSaveRequest(

	@NotBlank(message = "스킬 이펙트를 입력해주세요.")
	@EnumValidator(enumClass = SkillEffect.class)
	@Size(message = "스킬 이펙트 입력은 최대 30글자 이내로만 가능합니다.", max = 30)
	@Schema(description = "저장할 스킬의 이펙트")
	String skillEffect,

	@NotBlank(message = "등급을 입력해주세요.")
	@Size(message = "스킬 등급 입력은 최대 30글자 이내로만 가능합니다.", max = 30)
	@EnumValidator(enumClass = Grade.class)
	@Schema(description = "저장할 스킬 등급")
	String grade,

	@NotBlank(message = "스킬 이름을 입력해주세요.")
	@Size(message = "스킬 이름 입력은 최대 30글자 이내로만 가능합니다.", max = 30)
	@Schema(description = "저장할 스킬 이름")
	String name,

	@NotBlank(message = "스킬 설명을 입력해주세요.")
	@Size(message = "스킬 설명 입력은 최대 150글자 이내로만 가능합니다.", max = 150)
	@Schema(description = "저장할 스킬 설명/묘사")
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
