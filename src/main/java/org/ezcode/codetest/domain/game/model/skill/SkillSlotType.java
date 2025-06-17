package org.ezcode.codetest.domain.game.model.skill;

import org.ezcode.codetest.domain.game.exception.GameException;
import org.ezcode.codetest.domain.game.exception.GameExceptionCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SkillSlotType {

	SLOT_1(1), SLOT_2(2), SLOT_3(3), BACKPACK(4);

	private final int order;

	public static SkillSlotType fromSlotNumber(int slotNumber) {

		return switch (slotNumber) {
			case 1 -> SLOT_1;
			case 2 -> SLOT_2;
			case 3 -> SLOT_3;
			default -> throw new GameException(GameExceptionCode.SKILL_SLOT_NOT_EXIST);
		};
	}
}
