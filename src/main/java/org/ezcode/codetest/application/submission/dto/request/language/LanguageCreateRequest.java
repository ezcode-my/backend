package org.ezcode.codetest.application.submission.dto.request.language;

import org.ezcode.codetest.domain.language.model.entity.Language;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "언어 생성 요청")
public record LanguageCreateRequest(

	@Schema(description = "언어 이름", example = "Java")
	@NotBlank(message = "언어는 필수 입력 값입니다.")
	String name,

	@Schema(description = "언어 버전", example = "17")
	@NotBlank(message = "버전은 필수 입력 값입니다.")
	String version,

	@Schema(description = "Judge0에서 사용하는 언어 ID", example = "62")
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
