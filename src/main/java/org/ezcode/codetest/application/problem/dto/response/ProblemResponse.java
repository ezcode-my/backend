package org.ezcode.codetest.application.problem.dto.response;

import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.model.enums.Category;
import org.ezcode.codetest.domain.problem.model.enums.Reference;

import lombok.Builder;

@Builder
public record ProblemResponse(

	Long id,

	String creator,

	Category category,

	String title,

	int score,

	String difficulty,

	Reference reference

) {

	public static ProblemResponse from(Problem problem) {

		if (problem == null) {
			throw new IllegalArgumentException("문제는 null 값이 아니어야 합니다.");
		}

		return ProblemResponse.builder()
			.id(problem.getId())
			.creator(problem.getCreator() != null ? problem.getCreator().getNickname() : "존재하지 않는 이름.")
			.category(problem.getCategory())
			.title(problem.getTitle())
			.score(problem.getScore())
			.difficulty(problem.getDifficulty())
			.reference(problem.getReference())
			.build();
	}
}
