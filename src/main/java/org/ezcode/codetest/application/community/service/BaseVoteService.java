package org.ezcode.codetest.application.community.service;

import org.ezcode.codetest.application.community.dto.response.VoteResponse;
import org.ezcode.codetest.domain.community.model.VoteResult;
import org.ezcode.codetest.domain.community.model.entity.BaseVote;
import org.ezcode.codetest.domain.community.model.enums.VoteType;
import org.ezcode.codetest.domain.community.service.BaseVoteDomainService;
import org.ezcode.codetest.domain.user.model.entity.User;
import org.ezcode.codetest.domain.user.service.UserDomainService;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class BaseVoteService<T extends BaseVote, D extends BaseVoteDomainService<T, ?>> {

	protected final D voteDomainService;
	private final UserDomainService userDomainService;

	protected VoteResponse manageVote(Long voterId, Long targetId, VoteType voteType) {

		User voter = userDomainService.getUserById(voterId);

		VoteResult voteResult = voteDomainService.manageVote(voter, targetId, voteType);

		if (voteResult.voteType() == VoteType.UP) {
			afterVote(voter, targetId);
		}

		return VoteResponse.from(voteResult);
	}

	@Transactional(readOnly = true)
	public VoteResponse getVoteStatus(Long userId, Long targetId) {

		VoteResult voteResult = voteDomainService.getVoteStatus(userId, targetId);

		return VoteResponse.from(voteResult);
	}

	protected abstract void afterVote(User voter, Long targetId);
}
