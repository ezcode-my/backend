package org.ezcode.codetest.application.notification.enums;

import lombok.Getter;

@Getter
public enum NotificationType {
	/* 커뮤니티 */
	COMMUNITY_REPLY("댓글"),					// 작성한 discussion, reply, solution 등에 댓글 달림
	COMMUNITY_DISCUSSION_VOTED_UP("자유글 추천"),					// 작성한 discussion, reply, solution 등에 추천 받음
	COMMUNITY_REPLY_VOTED_UP("댓글 추천"),					// 작성한 discussion, reply, solution 등에 추천 받음
	COMMUNITY_MENTIONED("멘션"),				// 누군가 discussion, reply에서 멘션함

	/* 코딩 문제 */
	SUBMISSION_COMPLETED("제출 완료"),	// 제출 및 채점 완료
	;

	private final String description;

	NotificationType(String description) {
		this.description = description;
	}
}
