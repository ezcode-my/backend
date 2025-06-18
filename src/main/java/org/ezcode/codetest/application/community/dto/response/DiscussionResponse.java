package org.ezcode.codetest.application.community.dto.response;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.*;

import org.ezcode.codetest.application.usermanagement.user.dto.response.SimpleUserInfoResponse;
import org.ezcode.codetest.domain.community.model.entity.Discussion;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "DiscussionResponse", description = "Discussion 조회 응답 DTO")
public record DiscussionResponse(
	@Schema(description = "Discussion 고유 ID", example = "123", requiredMode = REQUIRED)
	Long discussionId,

	@Schema(description = "작성자 정보", requiredMode = REQUIRED)
	SimpleUserInfoResponse userInfo,

	@Schema(description = "관련 문제 ID", example = "45", requiredMode = REQUIRED)
	Long problemId,

	@Schema(description = "사용 언어명", example = "Java 17", requiredMode = REQUIRED)
	String languages,

	@Schema(description = "토론 내용", example = "이 문제는 이렇게 풀 수 있습니다...", requiredMode = REQUIRED)
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
