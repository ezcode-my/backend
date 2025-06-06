package org.ezcode.codetest.application.community.dto.request;

import org.ezcode.codetest.domain.community.model.Discussion;
import org.ezcode.codetest.domain.submission.model.entity.Language;
import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.user.model.entity.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DiscussionCreateRequest(

	@NotNull(message = "languageId는 필수값입니다.")
	Long languageId,

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
