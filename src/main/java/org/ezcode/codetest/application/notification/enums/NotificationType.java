package org.ezcode.codetest.application.notification.enums;

import java.util.Arrays;

import lombok.Getter;

@Getter
public enum NotificationType {
	/* 커뮤니티 */
	COMMUNITY_REPLY("댓글"),					// 작성한 discussion, reply, solution 등에 댓글 달림
	COMMUNITY_VOTED_UP("추천"),					// 작성한 discussion, reply, solution 등에 추천 받음
	COMMUNITY_MENTIONED("멘션"),				// 누군가 discussion, reply에서 멘션함

	/* 코딩 문제 */
	SUBMISSION_COMPLETED("제출 완료"),	// 제출 및 채점 완료
	;

	private final String description;

	NotificationType(String description) {
		this.description = description;
	}

	public static NotificationType from(String notificationType) {
		return Arrays.stream(NotificationType.values())
			.filter(n -> n.name().equalsIgnoreCase(notificationType))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("Invalid input : " + notificationType));
	}
}
