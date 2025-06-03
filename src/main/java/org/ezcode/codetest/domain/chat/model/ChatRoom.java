package org.ezcode.codetest.domain.chat.model;

import static jakarta.persistence.FetchType.*;

import org.ezcode.codetest.common.base.entity.BaseEntity;
import org.ezcode.codetest.domain.user.model.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chat_room")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	private String title;

	private Boolean isDeleted;

	@Builder
	public ChatRoom(User user, String title, Boolean isDeleted) {

		this.user = user;
		this.title = title;
		this.isDeleted = isDeleted;
	}

	public boolean isOwner(Long userId) {

		return user.getId().equals(userId);
	}

	public void deleteChatRoom() {

		isDeleted = true;
	}

}
