package org.ezcode.codetest.application.community.dto.request;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.*;

import org.ezcode.codetest.domain.community.model.Discussion;
import org.ezcode.codetest.domain.language.model.entity.Language;
import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.user.model.entity.User;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(name = "DiscussionCreateRequest", description = "새로운 Discussion 생성 요청 DTO")
public record DiscussionCreateRequest(

	@Schema(description = "문제에 사용할 언어 ID", example = "1", requiredMode = REQUIRED)
	@NotNull(message = "languageId는 필수값입니다.")
	Long languageId,

	@Schema(description = "토론 내용", example = "이 문제에 대해 이렇게 생각했습니다...", maxLength = 2000, requiredMode = REQUIRED)
	@NotBlank(message = "내용을 입력해야 합니다.")
	@Size(max = 2000, message = "내용은 최대 2000자까지 허용됩니다.")
	String content

) {
	public static Discussion toEntity(
		User user,
		Problem problem,
		Language language,
		DiscussionCreateRequest request
	) {
		return Discussion.builder()
			.user(user)
			.problem(problem)
			.language(language)
			.content(request.content())
			.build();
	}
}
