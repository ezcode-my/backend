package org.ezcode.codetest.domain.game.model.Character;

import lombok.Getter;

@Getter
public enum Stat {

	PROBLEM_SOLVING("문제해결 능력치"),
	DATA_STRUCTURE("자료구조해결 능력치"),
	OPTIMIZATION("최적화 능력치"),
	SPEED("속도"),
	DEBUGGING("디버깅 능력치");

	private final String stat;

	Stat(String stat) {
		this.stat = stat;
	}
}
