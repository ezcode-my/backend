package org.ezcode.codetest.application.problem.dto.response;

import org.ezcode.codetest.domain.problem.model.enums.Category;
import org.ezcode.codetest.domain.problem.model.enums.Reference;

public record ProblemResponse(

	Long id,

	String creator,

	Category category,

	String title,

	int score,

	String difficulty,

	Reference reference

) {

}
