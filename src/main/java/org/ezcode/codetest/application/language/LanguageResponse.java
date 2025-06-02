package org.ezcode.codetest.application.language;

import org.ezcode.codetest.domain.problem.model.entity.Language;

import lombok.Builder;

@Builder
public record LanguageResponse (

	Long id,

	String name,

	String version,

	Long judge0Id

){
	public static LanguageResponse from(Language language) {
		return LanguageResponse.builder()
			.id(language.getId())
			.name(language.getName())
			.version(language.getVersion())
			.judge0Id(language.getJudge0Id())
			.build();
	}
}
