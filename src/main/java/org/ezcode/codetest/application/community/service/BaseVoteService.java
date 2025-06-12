package org.ezcode.codetest.application.community.service;

import org.ezcode.codetest.application.community.dto.response.VoteResponse;
import org.ezcode.codetest.domain.community.model.BaseVote;
import org.ezcode.codetest.domain.community.service.BaseVoteDomainService;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class BaseVoteService<T extends BaseVote, D extends BaseVoteDomainService<T, ?>> {

	protected final D domainService;

	protected VoteResponse toggleVote(User voter, Long targetId) {

		boolean isVoted = domainService.toggleVote(voter, targetId);

		if (isVoted) {
			afterVote(voter, targetId);
		}

		return VoteResponse.of(isVoted);
	}

	@Transactional(readOnly = true)
	public VoteResponse getVoteStatus(Long userId, Long targetId) {

		boolean voteStatus = domainService.getVoteStatus(userId, targetId);
		return new VoteResponse(voteStatus);
	}

	protected abstract void afterVote(User voter, Long targetId);
}
