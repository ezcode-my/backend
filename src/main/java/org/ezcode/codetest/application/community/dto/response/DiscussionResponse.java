package org.ezcode.codetest.application.community.dto.response;

import org.ezcode.codetest.application.usermanagement.user.dto.response.SimpleUserInfoResponse;
import org.ezcode.codetest.domain.community.model.Discussion;

public record DiscussionResponse(

	Long discussionId,

	SimpleUserInfoResponse userInfo,

	Long problemId,

	String languages,

	String content

) {

	public static DiscussionResponse fromEntity(Discussion discussion) {
		return new DiscussionResponse(
			discussion.getId(),
			SimpleUserInfoResponse.fromEntity(discussion.getUser()),
			discussion.getProblem().getId(),	// 문제 id가 굳이 필요한가?
			discussion.getLanguage().getName(),	// TODO: 가공해줘야 할듯?
			discussion.getContent()
		);
	}

}
