package org.ezcode.codetest.application.language;

import org.ezcode.codetest.domain.problem.model.entity.Language;

public record LanguageRequest (

	String name,

	String version

){
	public static Language toEntity(LanguageRequest request) {
		return Language.builder()
			.name(request.name())
			.version(request.version())
			.build();
	}
}
