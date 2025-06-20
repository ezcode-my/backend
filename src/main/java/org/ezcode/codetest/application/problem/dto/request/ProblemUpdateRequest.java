package org.ezcode.codetest.application.problem.dto.request;

import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.model.enums.Category;
import org.ezcode.codetest.domain.problem.model.enums.Difficulty;
import org.ezcode.codetest.domain.problem.model.enums.Reference;
import org.ezcode.codetest.domain.user.model.entity.User;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProblemUpdateRequest(

	@Schema(description = "카테고리", example = "FOR_BEGINNER")
	Category category,

	@Schema(description = "제목", example = "A+B")
	String title,

	@Schema(description = "카테고리", example = "입력한 두수의 값을 더하고, 결과값을 출력하세요")
	String description,

	@Schema(description = "난이도", example = "BRONZE")
	Difficulty difficulty,

	@Schema(description = "메모리 제한", example = "30000")
	Long memoryLimit,

	@Schema(description = "시간 제한", example = "1000.0")
	Long timeLimit,

	@Schema(description = "출처", example = "ORIGINAL")
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
