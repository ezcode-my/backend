package org.ezcode.codetest.application.problem.dto.request;

import org.ezcode.codetest.domain.problem.model.entity.Language;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LanguageCreateRequest(

	@NotBlank(message = "언어는 필수 입력 값입니다.")
	String name,

	@NotBlank(message = "버전은 필수 입력 값입니다.")
	String version,

	@NotNull(message = "Judge0 아이디는 필수 입력 값입니다.")
	Long judge0Id

){
	public static Language toEntity(LanguageCreateRequest request) {
		return Language.builder()
			.name(request.name)
			.version(request.version)
			.judge0Id(request.judge0Id)
			.build();
	}
}
