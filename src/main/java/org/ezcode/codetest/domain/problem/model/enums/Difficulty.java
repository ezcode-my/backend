package org.ezcode.codetest.domain.problem.model.enums;

import java.util.Arrays;

import org.ezcode.codetest.domain.problem.exception.ProblemException;
import org.ezcode.codetest.domain.problem.exception.code.ProblemExceptionCode;

import lombok.Getter;

@Getter
public enum Difficulty {
	LV1("1", 10),
	LV2("2", 20),
	LV3("3", 40),
	LV4("4", 80),
	LV5("5", 160),
	LV6("6", 320),
	LV7("7",640);

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