package org.ezcode.codetest.infrastructure.session.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RedisKeyConstants {

	public static final String CHAT_COUNT_KEY_PREFIX = "session:chat:count:";
	public static final String BLOCKED_SESSION_KEY_PREFIX = "session:blocked:";

	public static final String CHATROOM_KEY_PREFIX = "chatroom:";
	public static final String SESSION_KEY_PREFIX = "session:";
	public static final String ROOM_COUNT_KEY_PREFIX = "roomCount:";

	public static final long CHAT_COUNT_TTL = 10L;
	public static final long BLOCKED_SESSION_TTL = 30L;

}
