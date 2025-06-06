package org.ezcode.codetest.application.submission.dto.response.language;

import org.ezcode.codetest.domain.submission.model.entity.Language;

public record LanguageResponse (

	Long id,

	String name,

	String version,

	Long judge0Id

){
	public static LanguageResponse from(Language language) {
		return new LanguageResponse(
			language.getId(), language.getName(), language.getVersion(), language.getJudge0Id()
		);
	}
}
