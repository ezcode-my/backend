package org.ezcode.codetest.domain.problem.model;

import org.ezcode.codetest.domain.problem.model.enums.Difficulty;

public record ProblemSearchCondition(

	String categoryCode,

	Difficulty difficulty,

	String keyword

) {

	public ProblemSearchCondition {

		categoryCode = cleanString(categoryCode);
		keyword = cleanString(keyword);
	}

	private static String cleanString(String input) {

		if (input == null || input.isBlank()) {
			return null;
		}
		return input.trim();
	}
}
