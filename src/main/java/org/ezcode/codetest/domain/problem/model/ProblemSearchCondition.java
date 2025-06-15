package org.ezcode.codetest.domain.problem.model;

import org.ezcode.codetest.domain.problem.model.enums.Category;

public record ProblemSearchCondition(
	Category category,

	String difficulty

) {
}
