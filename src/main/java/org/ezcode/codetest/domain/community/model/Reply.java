package org.ezcode.codetest.domain.community.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.ezcode.codetest.common.base.entity.BaseEntity;
import org.ezcode.codetest.domain.user.model.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reply extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "discussion_id", nullable = false)
	private Discussion discussion;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	// 부모 댓글 (NULL 가능)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_reply_id")
	private Reply parent;

	@Column(nullable = false)
	private String content;

	@Column(nullable = false)
	private boolean isDeleted;

	@Builder
	public Reply(Discussion discussion, User user, Reply parent, String content) {
		this.discussion = discussion;
		this.user = user;
		this.parent = parent;
		this.content = content;
	}

	public void update(String content) {
		this.content = content;
	}

	public void setDeleted() {
		this.isDeleted = true;
	}

	public boolean isDiscussionMatches(Long discussionId) {
		return Objects.equals(this.discussion.getId(), discussionId);
	}

	public boolean isAuthor(Long userId) {
		return Objects.equals(this.user.getId(), userId);
	}

	// Getter
	public User getParentReplyUser() {
		return this.parent.getUser();
	}

	public String getUserEmail() {
		return this.user.getEmail();
	}

	public Long getDiscussionId() {
		return this.discussion.getId();
	}

	public Long getProblemId() {
		return this.discussion.getProblemId();
	}

	public List<User> generateNotificationTargets() {

		Set<User> targets = new HashSet<>(); 	// 중복 방지를 위해 Set 사용

		User discussionAuthor = discussion.getUser();
		User parentAuthor = this.getParent() != null ? this.getParentReplyUser() : null;

		if (!this.getUser().shouldSkipNotification(discussionAuthor)) {
			targets.add(discussionAuthor);
		}

		if (!this.getUser().shouldSkipNotification(parentAuthor)) {
			targets.add(parentAuthor);
		}

		return new ArrayList<>(targets);
	}
}
