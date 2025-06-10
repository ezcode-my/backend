package org.ezcode.codetest.application.notification.event.payload;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
	use = JsonTypeInfo.Id.NAME,		// 타입 식별자를 이름으로 사용
	include = JsonTypeInfo.As.PROPERTY,		// 별도의 프로퍼티로 타입 식별자 추가
	property = "@type"	// 타입 식별자 프로퍼티의 이름 (예: "@type": "reply_created")
)
@JsonSubTypes({
	@JsonSubTypes.Type(value = ReplyCreatePayload.class, name = "reply_created"),
	@JsonSubTypes.Type(value = DiscussionVotePayload.class, name = "discussion_vote"),
	@JsonSubTypes.Type(value = ReplyVotePayload.class, name = "reply_vote")
})
public interface NotificationPayload {
}
