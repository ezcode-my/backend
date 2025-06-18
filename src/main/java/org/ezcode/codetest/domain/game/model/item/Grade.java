package org.ezcode.codetest.domain.game.model.item;

import lombok.Getter;

@Getter
public enum Grade {

	LEGENDARY("전설"), UNIQUE("유일"), RARE("희귀"), UNCOMMON("고급"), COMMON("일반"), TRASH("쓰레기");

	private final String grade;

	Grade(String grade) { this.grade = grade; }
}
