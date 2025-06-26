package org.ezcode.codetest.domain.problem.model.enums;

import java.util.Arrays;

import org.ezcode.codetest.domain.problem.exception.ProblemException;
import org.ezcode.codetest.domain.problem.exception.code.ProblemExceptionCode;

import lombok.Getter;

@Getter
public enum Difficulty {
	LV1("레벨1", 10),
	LV2("레벨2", 20),
	LV3("레벨3", 40),
	LV4("레벨4", 80),
	LV5("레벨5", 160),
	LV6("레벨6", 320),
	LV7("레벨7", 640);

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