package org.ezcode.codetest.domain.game.model.skill;

import static jakarta.persistence.FetchType.*;

import org.ezcode.codetest.common.base.entity.BaseEntity;
import org.ezcode.codetest.domain.game.model.character.GameCharacter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
	name = "game_character_skill",
	uniqueConstraints = @UniqueConstraint(
		name = "uk_character_skill",
		columnNames = {"game_character_id", "skill_id"}
	)
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GameCharacterSkill extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "game_character_id", nullable = false)
	private GameCharacter character;

	@ManyToOne(fetch = LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "skill_id", nullable = false)
	private Skill skill;

	@Enumerated(EnumType.STRING)
	private SkillSlotType slotType;

	@Builder
	public GameCharacterSkill(GameCharacter character, Skill skill, SkillSlotType slotType) {
		this.character = character;
		this.skill = skill;
		this.slotType = slotType;
	}

	public void equipSkill(SkillSlotType slotType) {

		this.slotType = slotType;
	}

	public void unEquipSkill() {

		this.slotType = SkillSlotType.BACKPACK;
	}
}
