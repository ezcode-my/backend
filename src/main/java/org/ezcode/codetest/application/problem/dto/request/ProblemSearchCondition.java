package org.ezcode.codetest.application.problem.dto.request;

import org.ezcode.codetest.domain.problem.model.enums.Category;
import org.ezcode.codetest.domain.problem.model.enums.Difficulty;

public record ProblemSearchCondition(
	Category category,

	Difficulty difficulty,

	String title
) {
}
