package org.ezcode.codetest.application.problem.dto.request;

import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.model.enums.Category;
import org.ezcode.codetest.domain.problem.model.enums.Difficulty;
import org.ezcode.codetest.domain.problem.model.enums.Reference;
import org.ezcode.codetest.domain.user.model.entity.User;

public record ProblemUpdateRequest(

	Category category,

	String title,

	String description,

	Difficulty difficulty,

	Long memoryLimit,

	String timeLimit,

	Reference reference

) {

	public static Problem from(ProblemUpdateRequest request, User user) {

		return Problem.builder()
			.creator(user)
			.category(request.category)
			.title(request.title)
			.description(request.description)
			.difficulty(request.difficulty.getDifficulty())
			.score(request.difficulty.getScore())
			.memoryLimit(request.memoryLimit)
			.timeLimit(request.timeLimit)
			.reference(request.reference)
			.build();
	}

}
