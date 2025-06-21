package org.ezcode.codetest.application.game.dto.response.skill;

import org.ezcode.codetest.domain.game.model.skill.GameCharacterSkill;
import org.ezcode.codetest.domain.game.model.skill.Skill;
import org.ezcode.codetest.domain.game.model.item.Grade;
import org.ezcode.codetest.domain.game.model.skill.SkillEffect;
import org.ezcode.codetest.domain.game.model.skill.SkillSlotType;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "스킬 조회 요청에 따른 응답")
public record SkillResponse (

	@Schema(description = "스킬의 이펙트")
	SkillEffect skillEffect,

	@Schema(description = "스킬 등급")
	Grade grade,

	@Schema(description = "스킬 이름")
	String name,

	@Schema(description = "스킬 설명")
	String skillDetails,

	@Schema(description = "장착한 상태라면 어느 슬롯에 장착되어있는지")
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
