package org.ezcode.codetest.application.problem.dto.response;

import java.time.LocalDateTime;

import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.model.enums.Category;
import org.ezcode.codetest.domain.problem.model.enums.Reference;

import lombok.Builder;

@Builder
public record ProblemDetailResponse(

	Long id,

	String creator,

	Category category,

	String title,

	String description,

	int score,

	String difficulty,

	String memoryLimit,

	int timeLimit,

	Reference reference,

	LocalDateTime createdAt,

	LocalDateTime modifiedAt,

	boolean isDeleted

) {

	public static ProblemDetailResponse from(Problem problem) {

		return ProblemDetailResponse.builder()
			.id(problem.getId())
			.creator(problem.getCreator().getNickname())
			.category(problem.getCategory())
			.title(problem.getTitle())
			.description(problem.getDescription())
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
