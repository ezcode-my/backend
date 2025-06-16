package org.ezcode.codetest.domain.community.model.entity;

import org.ezcode.codetest.domain.user.model.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reply_vote",
	uniqueConstraints = {
		@UniqueConstraint(
			name = "unique_voter_reply",
			columnNames = {
				"voter_id",
				"reply_id"
			}
		)
	})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReplyVote extends BaseVote {

	@ManyToOne
	@JoinColumn(name = "reply_id", nullable = false)
	private Reply reply;

	@Builder
	public ReplyVote(User voter, Reply reply) {
		this.voter = voter;
		this.reply = reply;
	}
}
