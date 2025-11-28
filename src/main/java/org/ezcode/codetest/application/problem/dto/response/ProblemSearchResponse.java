package org.ezcode.codetest.application.problem.dto.response;

import java.util.List;

import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.problem.model.entity.ProblemSearchDocument;

import lombok.Builder;

@Builder
public record ProblemSearchResponse(

	Long id,

	String title,

	List<String> category,

	String difficulty,

	String reference,

	String description,

	int score

) {
	public static ProblemSearchResponse from(ProblemSearchDocument document) {

		return ProblemSearchResponse.builder()
			.id(document.getId())
			.title(document.getTitle())
			.category(document.getCategories())
			.difficulty(document.getDifficulty())
			.reference(document.getReference().toString())
			.description(document.getDescription())
			.score(document.getScore())
			.build();
	}

	public static ProblemSearchResponse from(Problem problem) {
		
		return ProblemSearchResponse.builder()
			.id(problem.getId())
			.title(problem.getTitle())
			.category(null)	// TODO: 카테고리 입력해줘야 함
			.difficulty(problem.getDifficulty().getDifficulty())
			.reference(problem.getReference().toString())
			.description(problem.getDescription())
			.score(problem.getScore())
			.build();
	}
}
