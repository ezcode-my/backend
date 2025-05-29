package org.ezcode.codetest.domain.community.model;

import java.util.ArrayList;
import java.util.List;

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
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
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

	// 자식(대댓글) 목록
	@OneToMany(mappedBy = "parent")
	private List<Reply> replies = new ArrayList<>();

	@Column(nullable = false)
	private String content;

	@Column(nullable = false)
	private boolean isDeleted;

}
