package org.ezcode.codetest.application.problem.dto.request;

import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.model.enums.Category;
import org.ezcode.codetest.domain.problem.model.enums.Difficulty;
import org.ezcode.codetest.domain.problem.model.enums.Reference;
import org.ezcode.codetest.domain.user.model.entity.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProblemCreateRequest(

	@NotNull(message = "카테고리를 설정해야 합니다.")
	Category category,

	@NotBlank(message = "문제 제목을 입력하세요.")
	String title,

	@NotBlank(message = "문제 설명을 입력하세요.")
	String description,

	@NotNull(message = "난이도를 설정해야 합니다.")
	Difficulty difficulty,

	@NotBlank(message = "메모리 제한을 설정해야 합니다.")
	String memoryLimit,

	int timeLimit,

	@NotNull(message = "출처를 명시해야 합니다.")
	Reference reference

) {

	// Dto -> Entity 변환
	public static Problem toEntity(ProblemCreateRequest request, User user) {

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
