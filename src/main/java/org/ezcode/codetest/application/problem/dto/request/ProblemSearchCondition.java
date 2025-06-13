package org.ezcode.codetest.application.problem.dto.request;

import org.ezcode.codetest.domain.problem.model.enums.Category;

public record ProblemSearchCondition(
	Category category,

	String difficulty

) {
}
