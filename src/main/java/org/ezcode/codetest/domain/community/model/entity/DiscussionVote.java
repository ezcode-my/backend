package org.ezcode.codetest.domain.community.model.entity;

import org.ezcode.codetest.domain.community.model.enums.VoteType;
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
@Table(name = "discussion_vote",
	uniqueConstraints = {
		@UniqueConstraint(
			name = "unique_voter_discussion",
			columnNames = {
				"voter_id",
				"discussion_id"
			}
		)
	})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DiscussionVote extends BaseVote {

	@ManyToOne
	@JoinColumn(name = "discussion_id", nullable = false)
	private Discussion discussion;

	@Builder
	public DiscussionVote(User voter, Discussion discussion, VoteType voteType) {
		this.voter = voter;
		this.discussion = discussion;
		this.voteType = voteType;
	}
}
