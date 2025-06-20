package org.ezcode.codetest.domain.game.model.item;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Grade {
	LEGENDARY(3, "전설"),
	UNIQUE(10, "유일"),
	RARE(25, "희귀"),
	UNCOMMON(50, "고급"),
	COMMON(100, "일반"),
	TRASH(110, "쓰레기");

	private final int threshold;
	private final String grade;

	public static Grade fromRoll(int roll) {

		for (Grade grade : values()) {
			if (grade == TRASH) break;
			if (roll < grade.threshold)
				return grade;
		}

		return COMMON;
	}
}
