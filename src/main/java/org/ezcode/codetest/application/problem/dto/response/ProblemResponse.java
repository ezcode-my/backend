package org.ezcode.codetest.application.problem.dto.response;

import java.util.List;

import org.ezcode.codetest.domain.problem.model.entity.Category;
import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.model.enums.Difficulty;
import org.ezcode.codetest.domain.problem.model.enums.Reference;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record ProblemResponse(

	@Schema(description = "PK", example = "1")
	Long id,

	@Schema(description = "출제자", example = "홍길동")
	String creator,

	@Schema(description = "카테고리", example = "FOR_BEGINNER")
	List<String> categories,

	@Schema(description = "제목", example = "A+B")
	String title,

	@Schema(description = "점수", example = "20")
	int score,

	@Schema(description = "난이도", example = "BRONZE")
	Difficulty difficulty,

	@Schema(description = "출처", example = "ORIGINAL")
	Reference reference,

	@Schema(description = "총 제출횟수", example = "0")
	Long totalSubmissions,

	@Schema(description = "성공한 횟수", example = "0")
	Long correctSubmissions
) {

	public static ProblemResponse from(Problem problem, List<Category> categories) {

		if (problem == null) {
			throw new IllegalArgumentException("문제는 null 값이 아니어야 합니다.");
		}

		return ProblemResponse.builder()
			.id(problem.getId())
			.creator(problem.getCreator() != null ? problem.getCreator().getNickname() : "존재하지 않는 이름.")
			.categories(categories.stream().map(Category::getKorName).toList())
			.title(problem.getTitle())
			.score(problem.getScore())
			.difficulty(problem.getDifficulty())
			.reference(problem.getReference())
			.totalSubmissions(problem.getTotalSubmissions())
			.correctSubmissions(problem.getCorrectSubmissions())
			.build();
	}
}
