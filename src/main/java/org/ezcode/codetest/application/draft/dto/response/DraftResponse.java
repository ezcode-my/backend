package org.ezcode.codetest.application.draft.dto.response;

import org.ezcode.codetest.domain.draft.model.entity.Draft;

import lombok.Builder;

@Builder
public record DraftResponse(
	Long problemId,
	Long languageId,
	String code,
	Long version
) {
	public static DraftResponse from(Draft draft) {
		return DraftResponse.builder()
			.problemId(draft.getProblem().getId())
			.languageId(draft.getLanguage().getId())
			.code(draft.getCode())
			.version(draft.getVersion())
			.build();
	}
}
