package org.ezcode.codetest.application.problem.dto.request;

import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.model.enums.Category;
import org.ezcode.codetest.domain.problem.model.enums.Difficulty;
import org.ezcode.codetest.domain.problem.model.enums.Reference;
import org.ezcode.codetest.domain.user.model.entity.User;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProblemCreateRequest(

	@NotNull(message = "카테고리를 설정해야 합니다.")
	@Schema(description = "카테고리", example = "FOR_BEGINNER")
	Category category,

	@NotBlank(message = "문제 제목을 입력하세요.")
	@Schema(description = "제목", example = "A+B")
	String title,

	@NotBlank(message = "문제 설명을 입력하세요.")
	@Schema(description = "설명", example = "입력한 두수의 값을 더하고, 결과값을 출력하세요")
	String description,

	@NotNull(message = "난이도를 설정해야 합니다.")
	@Schema(description = "난이도", example = "BRONZE")
	Difficulty difficulty,

	@NotNull(message = "메모리 제한을 설정해야 합니다.")
	@Schema(description = "메모리 제한", example = "30000")
	Long memoryLimit,

	@NotNull(message = "시간 제한을 설정해야 합니다.")
	@Schema(description = "시간 제한", example = "1000.0")
	Double timeLimit,

	@NotNull(message = "출처를 명시해야 합니다.")
	@Schema(description = "출처", example = "ORIGINAL")
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
