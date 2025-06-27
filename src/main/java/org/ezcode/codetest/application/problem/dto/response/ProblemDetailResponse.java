package org.ezcode.codetest.application.problem.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import org.ezcode.codetest.domain.problem.model.entity.Category;
import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.model.enums.Difficulty;
import org.ezcode.codetest.domain.problem.model.enums.Reference;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record ProblemDetailResponse(

	@Schema(description = "PK", example = "1")
	Long id,

	@Schema(description = "출제자", example = "홍길동")
	String creator,

	@Schema(description = "카테고리", example = "FOR_BEGINNER")
	List<String> categories,

	@Schema(description = "제목", example = "A+B")
	String title,

	@Schema(description = "설명", example = "입력한 두수의 값을 더하고, 결과값을 출력하세요")
	String description,

	@Schema(description = "점수", example = "20")
	int score,

	@Schema(description = "난이도", example = "BRONZE")
	Difficulty difficulty,

	@Schema(description = "메모리 제한(KB)", example = "30000")
	Long memoryLimit,

	@Schema(description = "시간 제한(ms)", example = "1000")
	Long timeLimit,

	@Schema(description = "출처", example = "ORIGINAL")
	Reference reference,

	@Schema(description = "생성일", example = "2025-06-10 20:19:59.730028")
	LocalDateTime createdAt,

	@Schema(description = "수정일", example = "2025-06-11 08:12:43.506032")
	LocalDateTime modifiedAt

) {

	public static ProblemDetailResponse from(Problem problem, List<Category> categories) {

		return ProblemDetailResponse.builder()
			.id(problem.getId())
			.creator(problem.getCreator().getNickname())
			.title(problem.getTitle())
			.description(problem.getDescription())
			.categories(categories.stream().map(Category::getKorName).toList())
			.score(problem.getScore())
			.difficulty(problem.getDifficulty())
			.memoryLimit(problem.getMemoryLimit())
			.timeLimit(problem.getTimeLimit())
			.reference(problem.getReference())
			.createdAt(problem.getCreatedAt())
			.modifiedAt(problem.getModifiedAt())
			.build();
	}
}
