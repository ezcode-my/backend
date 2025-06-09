package org.ezcode.codetest.domain.problem.model.enums;

import java.util.Arrays;

import org.ezcode.codetest.domain.problem.exception.ProblemException;
import org.ezcode.codetest.domain.problem.exception.code.ProblemExceptionCode;

import lombok.Getter;

@Getter
public enum Difficulty {
	BRONZE("브론즈", 20),
	SILVER("실버", 40),
	GOLD("골드", 60),
	PLATINUM("플래티넘", 80),
	DIAMOND("다이아", 100);

	private final String difficulty;

	private final int score;

	Difficulty(String difficulty, int score) {
		this.difficulty = difficulty;
		this.score = score;
	}

	public static Difficulty getDifficultyFromKor(String difficulty) {

		return Arrays
			.stream(values())
			.filter(en -> en.getDifficulty().equals(difficulty))
			.findFirst()
			.orElseThrow(() -> new ProblemException(ProblemExceptionCode.DIFFICULTY_NOT_FOUND));
	}

}