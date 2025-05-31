package org.ezcode.codetest.domain.chat.model;

import org.ezcode.codetest.common.base.entity.BaseEntity;
import org.ezcode.codetest.domain.user.model.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
	name = "chat",
	indexes = @Index(name = "idx_chat_roomid_createdat", columnList = "room_id, created_at")
)
public class Chat extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne
	@JoinColumn(name = "room_id", nullable = false)
	private ChatRoom chatRoom;

	private String message;

	@Builder
	public Chat(User user, ChatRoom chatRoom, String message) {
		this.user = user;
		this.chatRoom = chatRoom;
		this.message = message;
	}
}
