package org.ezcode.codetest.application.chatting.service;

public enum ChatMessageTemplate {

	SPAM_BLOCK("%s 님께서 지나친 도배로 %d초 동안 차단되었습니다."),
	CHAT_ROOM_LEFT("%s 님께서 채팅방을 나가셨습니다~!"),
	CHAT_ROOM_ENTER("%s 님께서 채팅방에 들어오셨습니다~!");

	private final String template;

	ChatMessageTemplate(String template) {
		this.template = template;
	}

	public String format(Object... args) {
		return String.format(template, args);
	}
}
