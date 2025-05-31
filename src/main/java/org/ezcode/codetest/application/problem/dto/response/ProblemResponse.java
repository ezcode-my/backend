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

		return ProblemResponse.builder()
			.id(problem.getId())
			.creator(problem.getCreator().getNickname())
			.category(problem.getCategory())
			.title(problem.getTitle())
			.score(problem.getScore())
			.difficulty(problem.getDifficulty())
			.reference(problem.getReference())
			.build();
	}
}
