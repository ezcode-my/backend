package org.ezcode.codetest.domain.community.model.entity;

import java.time.LocalDateTime;

import org.ezcode.codetest.domain.community.model.enums.VoteType;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseVote {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;

	@ManyToOne
	@JoinColumn(name = "voter_id", nullable = false)
	protected User voter;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	protected VoteType voteType;

	@CreatedDate
	@Column(name = "created_at", nullable = false, updatable = false)
	protected LocalDateTime createdAt;

	public void updateVoteType(VoteType voteType) {
		this.voteType = voteType;
	}
}
