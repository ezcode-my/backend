package org.ezcode.codetest.application.problem.dto.response;

import org.ezcode.codetest.domain.problem.model.entity.ProblemSearchDocument;

import lombok.Builder;

@Builder
public record ProblemSearchResponse(

	Long id,

	String title,

	String category,

	String difficulty,

	String reference,

	String description,

	int score

) {
	public static ProblemSearchResponse from(ProblemSearchDocument document) {

		return ProblemSearchResponse.builder()
			.id(document.getId())
			.title(document.getTitle())
			.category(document.getCategories().toString())
			.difficulty(document.getDifficulty())
			.reference(document.getReference().toString())
			.description(document.getDescription())
			.score(document.getScore())
			.build();
	}
}
