package org.ezcode.codetest.application.community.dto.response;

import org.ezcode.codetest.domain.community.model.Discussion;

public record DiscussionResponse(

	Long discussionId,

	Long userId,

	Long problemId,

	String languages,

	String content

) {

	public static DiscussionResponse fromEntity(Discussion discussion) {

	}

}
