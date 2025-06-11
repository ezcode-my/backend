package org.ezcode.codetest.application.submission.dto.response.language;

import org.ezcode.codetest.domain.language.model.entity.Language;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "언어 응답 정보")
public record LanguageResponse (

	@Schema(description = "언어 ID", example = "1")
	Long id,

	@Schema(description = "언어 이름", example = "Java")
	String name,

	@Schema(description = "언어 버전", example = "17")
	String version,

	@Schema(description = "Judge0에서 사용하는 언어 ID", example = "62")
	Long judge0Id

){
	public static LanguageResponse from(Language language) {
		return new LanguageResponse(
			language.getId(), language.getName(), language.getVersion(), language.getJudge0Id()
		);
	}
}
