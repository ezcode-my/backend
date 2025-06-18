package org.ezcode.codetest.application.notification.enums;

import lombok.Getter;

@Getter
public enum NotificationType {
	/* 커뮤니티 */
	COMMUNITY_REPLY("새로운 댓글이 달렸습니다.", "/problems/{problemId}/discussions/{discussionId}"),
	COMMUNITY_DISCUSSION_VOTED_UP("토론글에 추천을 받았습니다.", "/problems/{problemId}/discussions/{discussionId}"),
	COMMUNITY_REPLY_VOTED_UP("댓글에 추천을 받았습니다.", "/problems/{problemId}/discussions/{discussionId}/replies/{replyId}"),
	COMMUNITY_MENTIONED("멘션", ""),
	;

	private final String message;

	private final String redirectUrl;

	NotificationType(String message, String redirectUrl) {
		this.message = message;
		this.redirectUrl = redirectUrl;
	}
}
