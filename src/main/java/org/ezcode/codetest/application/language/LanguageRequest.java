package org.ezcode.codetest.application.language;

import org.ezcode.codetest.domain.problem.model.entity.Language;

import jakarta.validation.constraints.NotBlank;

public record LanguageRequest (

	@NotBlank(message = "언어는 필수 입력 값입니다.")
	String name,

	@NotBlank(message = "버전은 필수 입력 값입니다.")
	String version

){
	public static Language toEntity(LanguageRequest request) {
		return Language.builder()
			.name(request.name())
			.version(request.version())
			.build();
	}
}
