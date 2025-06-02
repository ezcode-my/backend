package org.ezcode.codetest.application.community.dto.request;

import org.ezcode.codetest.domain.community.model.Discussion;
import org.ezcode.codetest.domain.problem.model.entity.Language;
import org.ezcode.codetest.domain.problem.model.entity.Problem;
import org.ezcode.codetest.domain.user.model.entity.User;

public record DiscussionCreateRequest(

	Long languageId,

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
