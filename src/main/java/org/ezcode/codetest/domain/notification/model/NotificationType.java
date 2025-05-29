package org.ezcode.codetest.domain.notification.model;

public enum NotificationType {
	/* 커뮤니티 */
	REPLY,					// 작성한 discussion, reply, solution 등에 댓글 달림
	VOTED_UP,				// 작성한 discussion, reply, solution 등에 추천 받음
	MENTIONED,				// 누군가 discussion, reply에서 멘션함

	/* 코딩 문제 */
	SUBMISSION_COMPLETED,	// 제출 및 채점 완료

	/* 채팅 */
	CHAT,					// 채팅 관련

}
